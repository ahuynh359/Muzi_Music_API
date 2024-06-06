package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    boolean existsByName(String name);


}