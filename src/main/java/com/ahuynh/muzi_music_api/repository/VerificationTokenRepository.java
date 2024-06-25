package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {


    VerificationToken findByUser(User user);

    VerificationToken findByToken(String token);
}
