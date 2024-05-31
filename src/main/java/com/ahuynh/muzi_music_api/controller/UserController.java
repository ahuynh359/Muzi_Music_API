package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("abc")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping()
    public String hello(){
        return "Hello World";
    }

}
