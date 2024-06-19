package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ForgotPassRequest implements Serializable {
    @Email
    @NotBlank
    private String email;
}
