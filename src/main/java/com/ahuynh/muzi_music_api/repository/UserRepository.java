package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.response.UserInfo;
import com.ahuynh.muzi_music_api.security.CustomUserDetail;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsernameOrEmail(String username, String email);
   Boolean existsByUsername(@NotBlank String username);
   Boolean existsByEmail(@NotBlank String email);



}
