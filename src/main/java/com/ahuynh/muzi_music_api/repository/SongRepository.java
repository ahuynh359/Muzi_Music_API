package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByName(String name);

    Optional<Song> findSongById(Long id);
}
