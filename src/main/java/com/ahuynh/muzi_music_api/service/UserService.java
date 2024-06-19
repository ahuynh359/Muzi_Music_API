package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.exception.PasswordException;
import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.role.Role;
import com.ahuynh.muzi_music_api.model.entity.role.RoleName;
import com.ahuynh.muzi_music_api.model.mapper.UserMapper;
import com.ahuynh.muzi_music_api.payload.request.UpdatePasswordRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateUserForAdmin;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final SongRepository songRepository;
    private final UserMapper userMapper;

//
//    public UserDto createUser(SignUpRequest signUpRequest) {
//        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new CustomException("Email already exists");
//        }
//
//        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
//            throw new CustomException("Username already exists");
//        }
//
//        //Lưu user vào db
//        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
//        Set<Role> roles = new HashSet<>();
//        if (userRepository.count() == 0) {
//            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
//                    .orElseThrow(() -> new CustomException("There is no role in db")));
//            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
//                    .orElseThrow(() -> new CustomException("There is no role in db")));
//
//        } else {
//            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
//                    .orElseThrow(() -> new CustomException("There is no role in db")));
//        }
//        User user = userRepository.save(new User(signUpRequest.getEmail(), encodedPassword, signUpRequest.getUsername(), roles));
//
//        return userMapper.convertToDto(user);
//    }

    public UserDto createUser(String email, String password, String username, MultipartFile avatar, boolean enable) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException("Email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new CustomException("Username already exists");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(password);
        Set<Role> roles = new HashSet<>();
        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));

        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
        }
        String url = firebaseService.upload(avatar, "image/png");
        User user = userRepository.save(new User(email, encodedPassword, username, roles, url, enable));
        return userMapper.convertToDto(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto updateUserForAdmin(UpdateUserForAdmin request) {
        User updateUser = userRepository.findById(request.getId()).orElseThrow(() ->
                new EntityNotFoundException("User not exits id =" + request.getId()));


        if (request.getUsername() != null) {
            updateUser.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            updateUser.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            updateUser.setHashPassword(passwordEncoder.encode(request.getPassword()));
        }


        return userMapper.convertToDto(userRepository.save(updateUser));
    }

    public UserDto unlock(Long id) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not exits id = " + id));
        updateUser.setEnabled(true);
        return userMapper.convertToDto(userRepository.save(updateUser));
    }

    public UserDto lock(Long id) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not exits id = " + id));
        updateUser.setEnabled(false);
        return userMapper.convertToDto(userRepository.save(updateUser));
    }

    public List<UserDto> getAllUser() {
        return userMapper.convertToDtoList(userRepository.findAll());
    }

    public UserDto getUserById(Long id) {
        return userMapper.convertToDto(userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not exits id =" + id)));
    }


    public UserDto updateAvatar(Long id, MultipartFile avatar) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found " + id));
        String url = firebaseService.upload(avatar, "image/png");
        user.setAvatar(url);
        return userMapper.convertToDto(userRepository.save(user));
    }

    public UserDto updatePassword(UpdatePasswordRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found " + request.getId()));
        if (!validatePassword(request.getOldPassword(), user.getHashPassword())) {
            throw new PasswordException("Old password don't match");
        }
        if (!(request.getNewPassword().equals(request.getConfirmPassword()))) {
            throw new PasswordException("Password and confirm password don't match");
        }
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setHashPassword(encodedPassword);

        return userMapper.convertToDto(userRepository.save(user));
    }


    private boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

//    public void loveSong(Long userId, Long songId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new ResourceNotFoundException("Song not found " + songId));
//        user.addLovedSong(song);
//        userRepository.save(user);
//    }
//
//    public void unloveSong(Long userId, Long songId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + userId));
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new ResourceNotFoundException("Song not found " + songId));
//        user.removeLovedSong(song);
//        userRepository.save(user);
//    }
//
//    public Set<Song> getLoveSong(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
//        return user.getLoveSongs();
//
//    }
//
//    public boolean isLoveSong(Long id, Long songId) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
//        Song song = songRepository.findById(songId)
//                .orElseThrow(() -> new ResourceNotFoundException("Song not found " + songId));
//        return user.getLoveSongs().contains(song);
//
//    }


//    public List<Playlist> getAllPlaylistById(Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
//        return userRepository.findPlaylistById(id);
//    }
//
//    public User getUserByUserName(String username) {
//        return userRepository.findByUsernameOrEmail(username, username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + username));
//    }
//
//
//    public User findByEmail(String email) {
//
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found " + email));
//    }
//
//

}
