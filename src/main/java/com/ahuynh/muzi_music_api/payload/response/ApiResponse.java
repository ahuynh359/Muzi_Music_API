package com.ahuynh.muzi_music_api.payload.response;

import io.jsonwebtoken.io.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse implements Serializable {

    private Boolean success;
    private String message;
    private Object data;



}
