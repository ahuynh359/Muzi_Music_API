package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service


public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private  UserRepository userRepository;

    public CustomUserDetail loadUserByUsername(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
                () -> new EntityNotFoundException("Invalid username or password")
        );
        return CustomUserDetail.createUserDetail(user);

    }

    public CustomUserDetail loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + id)
        );
        return CustomUserDetail.createUserDetail(user);
    }


}