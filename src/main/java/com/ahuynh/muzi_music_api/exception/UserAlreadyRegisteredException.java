package com.ahuynh.muzi_music_api.exception;

public class UserAlreadyRegisteredException extends CustomException{
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}