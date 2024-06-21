package com.ahuynh.muzi_music_api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTypeRequest {
    private Long id;
    private String name;
}
