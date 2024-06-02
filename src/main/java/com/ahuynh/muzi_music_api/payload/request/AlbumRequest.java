package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumRequest {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String avatar;

}
