package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);

    boolean existsByName(String name);

    @Query(value = "SELECT type.songs FROM Type type where type.id = :id")
    List<Song> findSongById(Long id);
}
