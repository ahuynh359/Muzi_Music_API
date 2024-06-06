package com.ahuynh.muzi_music_api.payload.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResendOtpRequest {
    private String email;
}
