package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.email.OnRegistrationCompleteEvent;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.verification.VerificationType;
import com.ahuynh.muzi_music_api.payload.request.*;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.AuthService;
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

    public String getCurrentUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping()
    public String test() {
        return "Hello World";
    }

    /**
     * Đăng kí - Gửi mã otp đến mail
     * Ai cũng được
     * Trả về message success
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request, final HttpServletRequest httpServletRequest) {
        User user = authService.createUser(request);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, getCurrentUrl(httpServletRequest), VerificationType.SIGN_IN));
        return new ResponseEntity<>(new MessageResponse("Sign in success please check email"), HttpStatus.OK);
    }

    /**
     * Xác thực email với otp sau khi đăng nhập
     * Ai cũng được
     * Trả về message success
     */
    @PostMapping("/verify/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {
        verificationTokenService.verifyEmail(token);
        return
                new ResponseEntity<>(new MessageResponse
                        ("Verify email successfully"), HttpStatus.OK);
    }

    /**
     * Đăng nhập
     * Ai cũng được
     * Trả về user đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return
                new ResponseEntity<>(new ApiResponse
                        ("Login successfully", authService.login(request)), HttpStatus.OK);
    }


    /**
     * Resend otp khi dang nhap
     * Ai cũng được
     * Tra ve message success
     */
    @PostMapping("/resend")
    public ResponseEntity<?> resendOtp(@Valid @RequestBody ResendOtpRequest request,
                                       final HttpServletRequest httpServletRequest) {
        User user = authService.resendOtp(request);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent
                (user, getCurrentUrl(httpServletRequest), VerificationType.SIGN_IN));
        return new ResponseEntity<>(
                new MessageResponse(
                        "Resent otp successfully"), HttpStatus.OK);
    }

    /**
     * Forgotpass -> gui email
     * Ai cũng được
     * Tra ve message success
     */
    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPassRequest request,
                                            final HttpServletRequest httpServletRequest) {
        User user = authService.forgotPassword(request);
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent
                (user, getCurrentUrl(httpServletRequest), VerificationType.FORGOT_PASSWORD));
        return new ResponseEntity<>(
                new MessageResponse(
                        "Resent otp successfully"), HttpStatus.OK);
    }

    /**
     * Reset new password
     * Ai cũng được
     * Tra ve message success
     */
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody NewPasswordRequest request
    ) {
        verificationTokenService.resetPassword(request);
        return
                new ResponseEntity<>(new MessageResponse
                        ("Change password successfully"), HttpStatus.OK);
    }


}