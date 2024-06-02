package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.email.OnRegistrationCompleteEvent;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.VerificationToken;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.request.LoginRequest;
import com.ahuynh.muzi_music_api.payload.request.SignUpRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageErrorResponse;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import com.ahuynh.muzi_music_api.security.JwtTokenProvider;
import com.ahuynh.muzi_music_api.service.UserService;
import com.ahuynh.muzi_music_api.service.VerificationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private ObjectMapper objectMapper;



    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, final HttpServletRequest request) {
        User user = userService.saveNewUser(signUpRequest);

        String basePath = "/ap1/v1";
        URI location = ServletUriComponentsBuilder.fromUriString(basePath).path("/users/{userId}").buildAndExpand(user.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, getCurrentUrl(request)));

        return new ResponseEntity<>(new ApiResponse(true, "Create User Successfully", objectMapper.convertValue(user, User.class)), headers, HttpStatus.OK);
    }

    @GetMapping("/verifyEmail/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if (verificationToken == null) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid Verification Token", null), HttpStatus.BAD_REQUEST);
        }
        if (verificationToken.getUser().isEnabled()) {
            return new ResponseEntity<>(new ApiResponse(false, "This account has already been verified", null), HttpStatus.BAD_REQUEST);

        }
        String verificationResult = verificationTokenService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("Valid")) {
            verificationTokenService.deleteToken(verificationToken);
            return new ResponseEntity<>(new ApiResponse(true, "Verified successfully", null), HttpStatus.OK);

        }
        return new ResponseEntity<>(new ApiResponse(false, "Invalid verification token", null), HttpStatus.BAD_REQUEST);
    }

    public String getCurrentUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserNameOrEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            return new ResponseEntity<>(new ApiResponse(true, "Verified successfully", jwt), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Invalid username or password", null), HttpStatus.UNAUTHORIZED);
        }
    }


}
