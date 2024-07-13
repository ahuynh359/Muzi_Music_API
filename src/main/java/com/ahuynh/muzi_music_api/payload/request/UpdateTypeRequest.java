package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateTypeRequest {
    private Long id;
    @NotBlank
    private String name;
}
