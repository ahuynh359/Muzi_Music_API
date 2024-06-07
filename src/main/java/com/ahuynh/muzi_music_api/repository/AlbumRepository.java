package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {


    boolean existsByName(String name);

    @Query(value = "SELECT album.songs FROM Album album where album.id = :id")
    List<Song> findSongById(Long id);

    Optional<Album> findByName(String name);
}