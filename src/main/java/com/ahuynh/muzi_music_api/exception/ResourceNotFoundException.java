package com.ahuynh.muzi_music_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {
    private String id;
    public ResourceNotFoundException( String id) {
        super(id);
    }


}
