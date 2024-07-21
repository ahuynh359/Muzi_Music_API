package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.exception.InvalidTokenException;
import com.ahuynh.muzi_music_api.exception.PasswordException;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.VerificationToken;
import com.ahuynh.muzi_music_api.payload.request.EmailRequest;
import com.ahuynh.muzi_music_api.payload.request.NewPasswordRequest;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {


    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);
    }


    public void resetPassword(NewPasswordRequest request) {

        VerificationToken token = verificationTokenRepository.findByToken(request.getOtp());
        if (token == null) {
            throw new InvalidTokenException("Token is invalid");
        }
        if (token.getExpiryTime().isBefore(Instant.now())) {
            throw new InvalidTokenException("Token is invalid");
        }
        User user = token.getUser();
        if (user.isLocked()) {
            throw new InvalidTokenException("This account is locked. Contact an administrator");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordException("Password and confirm password don't match");
        }

        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setHashPassword(encodedPassword);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public User sendEmail(EmailRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getVerificationToken() != null) {
            verificationTokenRepository.delete(user.getVerificationToken());
            user.setVerificationToken(null);
        }

        return userRepository.save(user);
    }
}


