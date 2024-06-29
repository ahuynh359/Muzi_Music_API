package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}