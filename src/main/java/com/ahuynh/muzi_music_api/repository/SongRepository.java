package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.Type;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Collection<Song> findByNameContainingIgnoreCase(@NotBlank String name);

    @Query(value = "SELECT song.comments FROM Song song where song.id = :id")
    Set<Comment> findAllCommentsFromSong(Long id);



    Page<Song> findByAlbumId(Long albumId, Pageable pageable);

    @Query("SELECT s FROM Song s JOIN s.types t WHERE t.id = :typeId")
    Page<Song> findByTypeId(@Param("typeId") Long typeId, Pageable pageable);

    Page<Song> findAllByOrderByNameDesc(Pageable pageable);

    Page<Song> findAllByOrderByNameAsc(Pageable pageable);

    Page<Song> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Song> findAllByOrderByCreatedAtAsc(Pageable pageable);

    Page<Song> findByNameContainingIgnoreCase(String query, Pageable pageable);
}
