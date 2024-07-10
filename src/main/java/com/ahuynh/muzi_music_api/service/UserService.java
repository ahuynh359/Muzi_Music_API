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
import com.ahuynh.muzi_music_api.payload.request.UpdateUserForAdmin;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FirebaseService firebaseService;
    private final UserMapper userMapper;


    public UserDto unlock(Long id) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not exits id = " + id));
        updateUser.setLocked(false);
        return userMapper.convertToDto(userRepository.save(updateUser));
    }

    public UserDto lock(Long id) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not exits id = " + id));
        updateUser.setLocked(true);
        return userMapper.convertToDto(userRepository.save(updateUser));
    }

    public List<UserDto> getAllUser() {
        return userMapper.convertToDtoList(userRepository.findAll());
    }


    public UserDto updateAvatar(CustomUserDetail currentUser, MultipartFile avatar) {
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));

        String url = firebaseService.upload(avatar, "image/png");
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
        if (currentUser.getAuthorities().contains(new SimpleGrantedAuthority(RoleName.ROLE_USER.name()))) {
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
}
