package com.ahuynh.muzi_music_api.exception;

import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<ApiResponse> handleCustomException(CustomException ex) {
        ApiResponse apiResponse = new ApiResponse(false, ex.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
