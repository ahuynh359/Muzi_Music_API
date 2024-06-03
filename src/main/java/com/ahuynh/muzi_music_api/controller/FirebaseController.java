package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/firebase")
public class FirebaseController {
    @Autowired
    private FirebaseService firebaseService;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile multipartFile) {
        return firebaseService.upload(multipartFile,"image/png");
    }
}
