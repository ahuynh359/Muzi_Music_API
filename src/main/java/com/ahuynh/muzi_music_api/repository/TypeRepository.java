package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Long> {
    Optional<Type> findByName(String name);

    boolean existsByName(String name);
}
