package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    private Long id;
    @NotBlank
    private String username;
    @Email
    @NotBlank
    private String email;

}
