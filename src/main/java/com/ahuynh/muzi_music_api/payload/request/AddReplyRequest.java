package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReplyRequest {
   private Long songId;
   private Long parentId;
   private String content;
}
