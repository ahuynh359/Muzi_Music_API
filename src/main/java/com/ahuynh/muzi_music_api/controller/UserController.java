package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.payload.request.AddUserRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdatePasswordRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateUserRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.UserService;
import com.ahuynh.muzi_music_api.utils.SortName;
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

    @PutMapping("/lock/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> lockOrUnlock(@PathVariable(name = "id") Long id, @CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Lock Or Unlock Successfully", userService.lockOrUnLock(id, currentUser)), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getNewUsers(@RequestParam(value = "sort", required = false, defaultValue = "NEW") SortName sort) {
        return new ResponseEntity<>(new ApiResponse("Get All Users Successfully", userService.getAllUsers(sort)),
                HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserById(@PathVariable Long id, @CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Success",
                userService.getUserById(id, currentUser)), HttpStatus.OK);
    }


    @PutMapping("/avatar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateAvatar(@PathVariable Long id, @CurrentUser CustomUserDetail currentUser,
                                          @RequestPart("avatar") MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse("Change Avatar Successfully",
                userService.updateAvatar(id, currentUser, avatar)), HttpStatus.OK);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest, @CurrentUser CustomUserDetail currentUser) {

        return new ResponseEntity<>(new ApiResponse("Update User Successfully",
                userService.updateUser(updateUserRequest, currentUser)), HttpStatus.OK);
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

        return new ResponseEntity<>(new ApiResponse("Create User Successfully", userService.createUser(request)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @CurrentUser CustomUserDetail currentUser) {
        userService.deleteUser(id, currentUser);
        return new ResponseEntity<>(new MessageResponse("Delete User Successfully"), HttpStatus.OK);
    }


}
