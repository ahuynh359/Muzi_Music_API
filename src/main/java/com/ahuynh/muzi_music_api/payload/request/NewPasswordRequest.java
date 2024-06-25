package com.ahuynh.muzi_music_api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class NewPasswordRequest implements Serializable {
    private String otp;
    private String newPassword;
    private String confirmPassword;
}
