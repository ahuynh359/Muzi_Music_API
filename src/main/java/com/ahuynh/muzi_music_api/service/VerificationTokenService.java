package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.exception.InvalidTokenException;
import com.ahuynh.muzi_music_api.exception.PasswordException;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.verification.VerificationToken;
import com.ahuynh.muzi_music_api.model.entity.verification.VerificationType;
import com.ahuynh.muzi_music_api.payload.request.NewPasswordRequest;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {


    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void saveVerificationToken(User user, String token, VerificationType type) {

        VerificationToken verificationToken = new VerificationToken(user, token, type);
        verificationTokenRepository.save(verificationToken);


    }

    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndType(token, VerificationType.SIGN_IN);
        if (verificationToken == null) {
            throw new InvalidTokenException("Token is invalid");
        }
        if (verificationToken.getUser().isEnabled()) {
            throw new InvalidTokenException("This account has been verified");
        }

        String verificationResult = validateToken(verificationToken);

        if (verificationResult.equalsIgnoreCase("Valid")) {
            return;
        }
        throw new InvalidTokenException("Invalid verification token");
    }

    private String validateToken(VerificationToken verificationToken) {
        User user = verificationToken.getUser();
        if (verificationToken.getExpiryTime().compareTo(Instant.now()) < 0) {
            verificationTokenRepository.delete(verificationToken);
            return "Token is expired";
        }
        user.setEnabled(true);
        verificationTokenRepository.delete(verificationToken);
        userRepository.save(user);
        return "Valid";
    }


    public void resetPassword(NewPasswordRequest request) {
        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndType(request.getOtp(), VerificationType.FORGOT_PASSWORD);
        if (verificationToken == null) {
            throw new InvalidTokenException("Token is invalid");
        }
        if (verificationToken.getUser().isLocked()) {
            throw new InvalidTokenException("This account is locked. Contact an administrator");
        }

        String verificationResult = validateToken(verificationToken);

        if (verificationResult.equalsIgnoreCase("Valid")) {

            User user = userRepository.findById(verificationToken.getUser().getId()).orElseThrow(() ->
                    new EntityNotFoundException("User not found with id: " + verificationToken.getUser().getId())
            );

            if (!validatePassword(request.getOldPassword(), user.getHashPassword())) {
                throw new PasswordException("Old password don't match");
            }
            if (!(request.getNewPassword().equals(request.getConfirmPassword()))) {
                throw new PasswordException("Password and confirm password don't match");
            }
            String encodedPassword = passwordEncoder.encode(request.getNewPassword());
            user.setHashPassword(encodedPassword);
            userRepository.save(user);
            return;
        }
        throw new InvalidTokenException("Invalid verification token");
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}