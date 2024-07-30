package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.exception.PasswordException;
import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.role.Role;
import com.ahuynh.muzi_music_api.model.entity.role.RoleName;
import com.ahuynh.muzi_music_api.model.mapper.UserMapper;
import com.ahuynh.muzi_music_api.payload.request.AddUserRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdatePasswordRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateUserRequest;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.utils.SortName;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FirebaseService firebaseService;
    private final UserMapper userMapper;


    public UserDto lockOrUnLock(Long id, CustomUserDetail customUserDetail) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not exits id = " + id));
        User user = userRepository.findById(customUserDetail.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + customUserDetail.getId()));
        if (user.getId().equals(id)) {
            throw new CustomException("You cannot lock or unlock yourself");
        }
        updateUser.setLocked(!updateUser.isLocked());
        return userMapper.convertToDto(userRepository.save(updateUser));
    }


    public List<UserDto> getAllUsers(SortName sort) {
        List<User> users = new ArrayList<>();
        switch (sort) {
            case A_Z -> {
                users = userRepository.findAllByOrderByUsernameAsc();
            }
            case Z_A -> {
                users = userRepository.findAllByOrderByUsernameDesc();
            }
            case NEW -> {
                users = userRepository.findAllByOrderByCreatedAtDesc();
            }
            case OLD -> {
                users = userRepository.findAllByOrderByCreatedAtAsc();
            }
            case LOCKED -> {
                users = userRepository.findByLockedTrue();
            }
            case UNLOCKED -> {
                users = userRepository.findByLockedFalse();
            }
            default -> userRepository.findAllByOrderByCreatedAtDesc();

        }

        users.removeIf(user -> user.getRole().getName().equals(RoleName.ROLE_ADMIN));
        return userMapper.convertToDtoList(users);
    }


    public UserDto updateAvatar(Long id, CustomUserDetail currentUser, MultipartFile avatar) {
        User curUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        if (curUser.getRole().getName().equals(RoleName.ROLE_USER) && !curUser.getId().equals(id)) {
            throw new CustomException("You cannot edit avatar");
        }
        String url = firebaseService.upload(avatar, "image/png");
        if (curUser.getRole().getName().equals(RoleName.ROLE_USER)) {
            curUser.setAvatar(url);
            return userMapper.convertToDto(userRepository.save(curUser));
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        user.setAvatar(url);
        return userMapper.convertToDto(userRepository.save(user));


    }

    public void updatePassword(UpdatePasswordRequest request, CustomUserDetail currentUser) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        if (!validatePassword(request.getOldPassword(), user.getHashPassword())) {
            throw new PasswordException("Old password don't match");
        }
        if (!(request.getNewPassword().equals(request.getConfirmPassword()))) {
            throw new PasswordException("Password and confirm password don't match");
        }
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setHashPassword(encodedPassword);

        userMapper.convertToDto(userRepository.save(user));
    }


    private boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public UserDto getUserById(Long id, CustomUserDetail currentUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + id));
        if (user.getRole().getName().equals(RoleName.ROLE_USER)) {
            if (!currentUser.getId().equals(id)) {
                throw new CustomException("You are not authorized to view this user");
            }
        }
        return userMapper.convertToDto(user);
    }

    public UserDto createUser(AddUserRequest request) {
        Role role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new EntityNotFoundException("Role not found " + RoleName.ROLE_USER));
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new EntityExistsException("User exist");
        }
        User user = new User(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername(), role);
        return userMapper.convertToDto(userRepository.save(user));
    }

    public void deleteUser(Long id, CustomUserDetail currentUser) {
        User deleteUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + id));
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        if (user.getId().equals(id)) {
            throw new CustomException("You cannot delete yourself");
        }
        userRepository.deleteById(id);
    }

    public UserDto updateUser(UpdateUserRequest updateUserRequest, CustomUserDetail customUserDetail) {
        User updateUser = userRepository.findById(updateUserRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + updateUserRequest.getId()));
        User user = userRepository.findById(customUserDetail.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + customUserDetail.getId()));
        if (user.getRole().getName().equals(RoleName.ROLE_USER) && !customUserDetail.getId().equals(updateUserRequest.getId())) {
            throw new CustomException("You cannot update other users");
        }
        updateUser.setEmail(updateUserRequest.getEmail());
        updateUser.setUsername(updateUserRequest.getUsername());
        return userMapper.convertToDto(userRepository.save(updateUser));
    }

    public UserDto updateToken(String token, CustomUserDetail currentUser) {
        User updateUser = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        updateUser.setDeviceToken(token);
        return userMapper.convertToDto(userRepository.save(updateUser));
    }
}
