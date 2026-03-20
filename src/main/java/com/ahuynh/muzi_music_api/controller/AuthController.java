package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.email.OnRegistrationCompleteEvent;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.payload.request.*;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.AuthService;
import com.ahuynh.muzi_music_api.service.UserService;
import com.ahuynh.muzi_music_api.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    public String getCurrentUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@Valid @RequestBody AddUserRequest request) {

        return new ResponseEntity<>(new ApiResponse("Create User Successfully", userService.createUser(request)), HttpStatus.OK);
    }

    @GetMapping()
    public String test() {
        return "Hello World";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request) {
        return new ResponseEntity<>(new ApiResponse("Sign Up Successfully",authService.createUser(request)), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return new ResponseEntity<>(new ApiResponse
                        ("Login Successfully", authService.login(request)), HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@Valid @RequestBody EmailRequest request,
                                            final HttpServletRequest httpServletRequest) {
        User user = verificationTokenService.sendEmail(request);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent
                (user, getCurrentUrl(httpServletRequest)));
        return new ResponseEntity<>(
                new MessageResponse(
                        "Resent Otp Successfully"), HttpStatus.OK);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody NewPasswordRequest request
    ) {
        verificationTokenService.resetPassword(request);
        return
                new ResponseEntity<>(new MessageResponse
                        ("Change Password Successfully"), HttpStatus.OK);
    }


}