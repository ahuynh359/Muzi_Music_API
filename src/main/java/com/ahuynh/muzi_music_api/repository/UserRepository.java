package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.entity.Playlist;
import com.ahuynh.muzi_music_api.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsernameOrEmail(String username, String email);
   User findUserByUsernameOrEmail(String username, String email);
   Boolean existsByUsername(@NotBlank String username);
   Boolean existsByEmail(@NotBlank String email);


   Optional<User> findByEmail(String email);

    default User getUser(CustomUserDetail currentUser){
       return getByEmail(currentUser.getEmail());
    }

    default User getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

}
