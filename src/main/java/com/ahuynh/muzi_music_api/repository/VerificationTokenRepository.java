package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.verification.VerificationToken;
import com.ahuynh.muzi_music_api.model.entity.verification.VerificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.javapoet.TypeName;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByTokenAndType(String token, VerificationType type);

    VerificationToken findByUserAndType(User user, VerificationType type);
}
