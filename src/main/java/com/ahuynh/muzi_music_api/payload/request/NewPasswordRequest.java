package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewPasswordRequest implements Serializable {
    private String otp;
    @Size(min = 6)
    private String newPassword;
    private String confirmPassword;
}
