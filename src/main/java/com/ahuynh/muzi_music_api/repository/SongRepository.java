package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByName(String name);

    Optional<Song> findSongById(Long id);


    @Query(value = "SELECT song.types FROM Song song where song.id = :id")
    Optional<List<Type>> findAllTypeById(Long id);

    List<Song> findTop10ByOrderByCreatedAtDesc();
}
