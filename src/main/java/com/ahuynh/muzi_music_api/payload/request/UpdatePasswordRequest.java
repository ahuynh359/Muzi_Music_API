package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    private Long id;
    private String oldPassword;
    @Size(min = 6)
    private String newPassword;
    private String confirmPassword;
}
