package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Listen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListenRepository extends JpaRepository<Listen, Long> {
    @Query("SELECT l.song.id, l.song.name, FUNCTION('DATE_FORMAT', l.listenTime, '%d-%m-%Y') as day, COUNT(l) as listenCount " +
            "FROM Listen l " +
            "GROUP BY l.song.id, l.song.name, day " +
            "ORDER BY COUNT(l) DESC")
    List<Object[]> findSongListenDetailsGroupedByDay();

}
