package com.ahuynh.muzi_music_api.model.dto;

import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.payload.response.CommentParentAndChildrenResponse;
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
    private String time;
    private Long commentParentId;
    private Set<CommentDto> replies = new HashSet<>();
    private String createdAt;
    private String updatedAt;


}
