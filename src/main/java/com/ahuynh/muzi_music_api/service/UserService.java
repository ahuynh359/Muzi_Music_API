package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.request.SignUpRequest;
import com.ahuynh.muzi_music_api.payload.request.UserRequest;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final FirebaseService firebaseService;


    public User saveNewUser(SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException("Email already exists");
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new CustomException("Username already exists");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        List<Role> roles = new ArrayList<>();
        if (userRepository.count() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));

        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException("There is no role in db")));
        }
        User user = new User(signUpRequest.getEmail(), encodedPassword, signUpRequest.getUsername(), roles);

        return userRepository.save(user);
    }

    public User save(String email, String password, String username, MultipartFile avatar) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException("Email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new CustomException("Username already exists");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(password);
        List<Role> roles = new ArrayList<>();
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
        User user = new User(email, encodedPassword, username, roles, url, true);

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not exits id =" + id));
        userRepository.deleteById(id);
    }

    public User updateUser(Long iid, UserRequest userRequest) {
        User updateUser = userRepository.findById(iid).orElseThrow(() ->
                new ResourceNotFoundException("User not exits id =" + iid));

        if (userRequest.getEmail() != null) {

            updateUser.setEmail(userRequest.getEmail());
        }
        if (userRequest.getUsername() != null) {

            updateUser.setUsername(userRequest.getUsername());
        }

        if (userRequest.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(userRequest.getPassword());

            updateUser.setPassword(encodedPassword);
        }


        return userRepository.save(updateUser);
    }

    public User updateEnable(Long id, boolean b) {
        User updateUser = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not exits id =" + id));
        updateUser.setEnabled(b);
        return userRepository.save(updateUser);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not exits id =" + id));
    }
}
