package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.model.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SingerRepository extends JpaRepository<Singer, Long> {
    @Query("SELECT s.songs FROM Singer s WHERE s.id = :id ")
    Collection<Song> findSongById(Long id);


    Page<Singer> findByNameContainingIgnoreCase(String query, Pageable pageable);

    Page<Singer> findAllByOrderByNameAsc(Pageable pageable);

    Page<Singer> findAllByOrderByNameDesc(Pageable pageable);

    Page<Singer> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Singer> findAllByOrderByCreatedAtAsc(Pageable pageable);

}
