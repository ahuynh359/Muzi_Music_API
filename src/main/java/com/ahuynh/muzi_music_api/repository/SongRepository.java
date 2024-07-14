package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.Type;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByName(String name);

    Optional<Song> findSongById(Long id);


    @Query(value = "SELECT song.types FROM Song song where song.id = :id")
    Optional<List<Type>> findAllTypeById(Long id);

    List<Song> findTop4ByOrderByCreatedAtDesc();

    Collection<Song> findByNameContainingIgnoreCase(@NotBlank String name);

    @Query(value = "SELECT song.comments FROM Song song where song.id = :id")
    List<Comment> findCommentById(Long id);

    List<Song> findAllByOrderByCreatedAtDesc();
}
