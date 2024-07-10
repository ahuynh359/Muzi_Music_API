package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserRequest {
    @Email
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String username;
}
