package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.VerificationToken;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.request.SignUpRequest;
import com.ahuynh.muzi_music_api.payload.response.UserInfo;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import com.ahuynh.muzi_music_api.security.CustomUserDetail;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public long countUser() {
        return userRepository.count();
    }

    public User save(User user) {
        return userRepository.save(user);
    }


    public User saveNewUser(SignUpRequest signUpRequest) {
        if (existsByEmail(signUpRequest.getEmail())) {
            throw new CustomException( "Email already exists");
        }

        if (existsByUsername(signUpRequest.getUsername())) {
            throw new CustomException("Username already exists");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        List<Role> roles = new ArrayList<>();
        if (countUser() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException( "There is no role in db")));
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException( "There is no role in db")));

        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException( "There is no role in db")));
        }
        User user = new User(signUpRequest.getEmail(), encodedPassword, signUpRequest.getUsername(), roles);

        return save(user);
    }
}
