package com.ahuynh.muzi_music_api.repository;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByOrderByContentAsc();

    List<Comment> findAllByOrderByContentDesc();

    List<Comment> findAllByOrderByCreatedAtDesc();

    List<Comment> findAllByOrderByCreatedAtAsc();
    @Query("SELECT DISTINCT c FROM Comment c LEFT JOIN FETCH c.replies WHERE c.song.id = :songId")
    List<Comment> findAllBySongId(@Param("songId") Long songId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Comment c WHERE c.commentParent.id = :parentId")
    void deleteByParentId(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Comment c WHERE c.commentParent.id = :parentId")
    Set<Comment> findAllByCommentParent(@Param("parentId") Long parentId);

    @Query("SELECT c FROM Comment c WHERE c.song.id = :songId AND c.commentParent.id IS NULL")
    List<Comment> findParentCommentsBySongId(@Param("songId") Long songId);
}
