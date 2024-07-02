package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Playlist;
import com.ahuynh.muzi_music_api.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    boolean existsByName(String name);


    Optional<Playlist> findByName(String name);

    boolean existsByUserId(Long userId);

    Optional<Playlist> findByIdAndUserId(Long id, Long userId);

    Optional<Playlist> findByNameAndUserId(String name, Long userId);

    List<Playlist> findAllByUserId(Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByNameAndUserId(String name, Long id);

    @Query("SELECT p.songs FROM Playlist p where p.id = :id")
    List<Song> findSongsById(Long id);
}