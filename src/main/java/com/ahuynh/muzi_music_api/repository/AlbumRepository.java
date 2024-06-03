package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findAlbumById(Long id);
    Optional<Album> findAlbumByName(String name);


    boolean existsByName(String name);
}