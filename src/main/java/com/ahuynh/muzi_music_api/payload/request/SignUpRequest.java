package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class SignUpRequest {
    @Email
    @NotNull
    private String email;
    @Size(min = 6)
    @NotNull
    private String password;
    @NotNull
    private String username;


}