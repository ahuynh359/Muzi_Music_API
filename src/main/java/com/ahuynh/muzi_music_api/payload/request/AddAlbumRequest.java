package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddAlbumRequest {
    @NotBlank
    private String name;

}
