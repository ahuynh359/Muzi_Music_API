package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.payload.request.AlbumRequest;
import com.ahuynh.muzi_music_api.payload.request.UserRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.UserResponse;
import com.ahuynh.muzi_music_api.service.FirebaseService;
import com.ahuynh.muzi_music_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUser(@RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("username") String username,
                                     @RequestParam("avatar") MultipartFile avatar) {

       User user = userService.save(email ,password , username,avatar);
        return new ResponseEntity<>(new ApiResponse(true, "Create Successfully",
                objectMapper.convertValue(user, User.class)), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete Successfully", null), HttpStatus.OK);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id,
                                        @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",   objectMapper.convertValue(user, User.class)), HttpStatus.OK);
    }


    @PutMapping("/enable/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> unlock(@PathVariable(name = "id") Long id) {
        User user = userService.updateEnable(id,true );
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",   objectMapper.convertValue(user, User.class)), HttpStatus.OK);
    }

    @PutMapping("/disable/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> lock(@PathVariable(name = "id") Long id) {
        User user = userService.updateEnable(id,false );
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",   objectMapper.convertValue(user, User.class)), HttpStatus.OK);
    }


    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllUser() {
        List<User> user = userService.getAllUser();
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", user), HttpStatus.OK);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",  objectMapper.convertValue(user, User.class)), HttpStatus.OK);
    }






}
