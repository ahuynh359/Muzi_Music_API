package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private Set<CommentDto> comments;
    private int  totalComments = 0 ;
}
