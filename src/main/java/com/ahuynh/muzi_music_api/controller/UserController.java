package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.UserInfo;
import com.ahuynh.muzi_music_api.security.CurrentUser;
import com.ahuynh.muzi_music_api.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping()
    public String hello(){
        return "Hello World";
    }



}
