package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@Repository
public interface SingerRepository extends JpaRepository<Singer, Long> {
    @Query("SELECT s.songs FROM Singer s WHERE s.id = :id ")
    Collection<Song> findSongById(Long id);

    Collection<Singer> findTop10ByOrderByCreatedAtDesc();

    Collection<Singer> findByNameContainingIgnoreCase(String query);

    List<Singer> findAllByOrderByNameAsc();

    List<Singer> findAllByOrderByNameDesc();

    List<Singer> findAllByOrderByCreatedAtDesc();

    List<Singer> findAllByOrderByCreatedAtAsc();
}
