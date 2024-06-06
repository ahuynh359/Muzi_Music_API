package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsernameOrEmail(String username, String email);
   Boolean existsByUsername(@NotBlank String username);
   Boolean existsByEmail(@NotBlank String email);

    @Query(value = "SELECT user.following FROM User user where user.id = :id")
    List<User> findFollowingById(Long id);

    @Query(value = "SELECT user.followers FROM User user where user.id = :id")
    List<User> findFollowerById(Long id);

    @Query(value = "SELECT user.loveSongs FROM User user where user.id = :id")
    List<Song> findLoveSongById(Long id);

    @Query(value = "SELECT user.playlist FROM User user where user.id = :id")
    List<Playlist> findPlaylistById(Long id);

    Optional<User> findByEmail( String email);
}
