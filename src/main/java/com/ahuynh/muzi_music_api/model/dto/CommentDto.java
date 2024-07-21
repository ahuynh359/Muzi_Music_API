package com.ahuynh.muzi_music_api.model.dto;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private UserDto user;
    private Set<CommentDto> replies = new HashSet<>();
    private String time;
    private boolean love;
    private String createdAt;
    private String updatedAt;


}
