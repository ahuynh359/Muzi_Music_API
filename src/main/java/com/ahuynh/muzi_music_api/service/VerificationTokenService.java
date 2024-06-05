package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.VerificationToken;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {


    private final VerificationTokenRepository verificationTokenRepository;


    private final UserRepository userRepository;


    public void saveVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);

    }

    public String validateToken(String token) {
        VerificationToken t = verificationTokenRepository.findByToken(token);
        if (token == null) {
            return "Invalid Token";
        }
        User user = t.getUser();
        if (t.getExpiryTime().compareTo(Instant.now()) < 0) {
            deleteToken(t);
            return "Token is expired";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Valid";
    }

    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}
