package com.ahuynh.muzi_music_api.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class VerifyEmailRequest implements Serializable {
    private String token;
    private String oldPassword;
    private String newPassword;
}
