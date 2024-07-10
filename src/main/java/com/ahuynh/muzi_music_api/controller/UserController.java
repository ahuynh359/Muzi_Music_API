package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.payload.request.AddUserRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdatePasswordRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateUserForAdmin;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/unlock/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> unlock(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Unlock Successfully", userService.unlock(id)), HttpStatus.OK);
    }

    @PutMapping("/lock/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> lock(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Lock Successfully", userService.lock(id)), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllUser() {
        return new ResponseEntity<>(new ApiResponse("Get All Users Successfully", userService.getAllUser()),
                HttpStatus.OK);
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id,@CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Success",
                userService.getUserById(id,currentUser)), HttpStatus.OK);
    }



    @PutMapping("/avatar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateAvatar(@CurrentUser CustomUserDetail currentUser,
                                          @RequestPart("avatar") MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Change Avatar Successfully",
                userService.updateAvatar(currentUser, avatar)), HttpStatus.OK);
    }


    @PutMapping("/password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request, @CurrentUser CustomUserDetail currentUser) {
        userService.updatePassword(request, currentUser);
        return new ResponseEntity<>(new MessageResponse("Change Password Successfully"), HttpStatus.OK);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@Valid @RequestBody AddUserRequest request) {

        return new ResponseEntity<>(new ApiResponse("Create User Successfully",userService.createUser(request)), HttpStatus.OK);
    }



}
