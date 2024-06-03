package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SongRequest {
    @NotBlank
    private String name;
    private String avatar;
    private String lyrics;
    private Long albumId;
}
