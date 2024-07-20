package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByContentAsc();

    List<Comment> findAllByOrderByContentDesc();

    List<Comment> findAllByOrderByCreatedAtDesc();

    List<Comment> findAllByOrderByCreatedAtAsc();
}
