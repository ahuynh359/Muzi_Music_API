package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {


}