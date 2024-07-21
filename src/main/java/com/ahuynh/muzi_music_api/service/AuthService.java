package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.config.security.JwtTokenProvider;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.exception.InvalidUserException;
import com.ahuynh.muzi_music_api.exception.UserAlreadyRegisteredException;
import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.VerificationToken;
import com.ahuynh.muzi_music_api.model.entity.role.Role;
import com.ahuynh.muzi_music_api.model.entity.role.RoleName;
import com.ahuynh.muzi_music_api.model.mapper.UserMapper;
import com.ahuynh.muzi_music_api.payload.request.ForgotPassRequest;
import com.ahuynh.muzi_music_api.payload.request.LoginRequest;
import com.ahuynh.muzi_music_api.payload.request.ResendOtpRequest;
import com.ahuynh.muzi_music_api.payload.request.SignUpRequest;
import com.ahuynh.muzi_music_api.payload.response.LoginResponse;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    public UserDto createUser(SignUpRequest request) {
        //Check exception
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityNotFoundException("This email already registered. Please try other email.");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new EntityNotFoundException("This username already registered. Please try other username.");
        }
        if (!request.getConfirmPassword().equals(request.getPassword())) {
            throw new InvalidUserException("Passwords do not match.");
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Role role;
        if (userRepository.count() == 0) {
            role = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new EntityNotFoundException("There is no role admin in db"));

        } else {
            role = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new EntityNotFoundException("There is no role user in db"));
        }
        return userMapper.convertToDto(userRepository.save(new User(request.getEmail(), encodedPassword, request.getUsername(), role)));
    }


    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserNameOrEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
            User user = userRepository.findUserByUsernameOrEmail(userDetails.getUsername(), userDetails.getEmail()).orElseThrow(() -> new InvalidUserException("Invalid username or password"));
            if (user.isLocked()) {
                throw new InvalidUserException("User is locked");
            }
            return new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), jwt, user.getRole().getName() == RoleName.ROLE_ADMIN);
        } catch (BadCredentialsException e) {
            throw new InvalidUserException("Invalid username or password");
        }

    }



}