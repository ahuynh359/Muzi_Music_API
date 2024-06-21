package com.ahuynh.muzi_music_api.payload.response;

import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentParentAndChildrenResponse {
    private CommentDto commentParent;
    private List<CommentDto> commentChildren;
}
