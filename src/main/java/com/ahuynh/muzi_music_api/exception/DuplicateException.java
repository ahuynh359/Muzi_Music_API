package com.ahuynh.muzi_music_api.exception;

public class DuplicateException extends RuntimeException {
    private String name;
    public DuplicateException( String name) {
        super(name);
    }


}