package com.ahuynh.muzi_music_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private String message;

    public CustomException( String message) {
        super(message);
    }


}
