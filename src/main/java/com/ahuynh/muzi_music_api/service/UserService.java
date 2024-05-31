package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends JpaRepository<User, Long> {
}
