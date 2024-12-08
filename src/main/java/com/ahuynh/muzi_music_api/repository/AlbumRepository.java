package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Album;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {


    boolean existsByName(String name);

    Optional<Album> findByName(String name);

    Page<Album> findByNameContainingIgnoreCase(@NotBlank String name, Pageable pageable);

    List<Album> findAllByOrderByNameAsc();

    List<Album> findAllByOrderByNameDesc();

    List<Album> findAllByOrderByCreatedAtDesc();

    List<Album> findAllByOrderByCreatedAtAsc();

    Page<Album> findAllByOrderByNameAsc(Pageable pageable);
    Page<Album> findAllByOrderByNameDesc(Pageable pageable);
    Page<Album> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Album> findAllByOrderByCreatedAtAsc(Pageable pageable);

}