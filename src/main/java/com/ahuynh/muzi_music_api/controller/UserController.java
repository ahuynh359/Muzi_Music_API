package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.entity.User;
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

    /**
     * Thêm người dùng
     * ADMIN
     * UserDto
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUser(@RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        @RequestParam("username") String username,
                                        @RequestParam("avatar") MultipartFile avatar,
                                        @RequestParam("enable") boolean enable) {

        return new ResponseEntity<>(new ApiResponse("Create Success",
                userService.createUser(email, password, username, avatar, enable)), HttpStatus.CREATED);
    }

    /**
     * Xóa người dùng
     * ADMIN
     * MessageResponse
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new MessageResponse("Delete Success"), HttpStatus.OK);
    }

    /**
     * Sửa người dùng cho admin ko co avatar
     * ADMIN
     * UserDto
     */
    @PutMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateUserForAdmin(
            @RequestBody UpdateUserForAdmin request) {
        return new ResponseEntity<>(new ApiResponse("Update Success",
                userService.updateUserForAdmin(request)), HttpStatus.OK);
    }


    /**
     * Mở khóa người dùng
     * ADMIN
     * UserDto
     */
    @PostMapping("/unlock/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> unlock(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Unlock Success", userService.unlock(id)), HttpStatus.OK);
    }

    /**
     * Khóa người dùng
     * ADMIN
     * UserDto
     */
    @PostMapping("/lock/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> lock(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Lock Success", userService.lock(id)), HttpStatus.OK);
    }

    /**
     * Lấy thông tin tất cả người dùng
     * ADMIN
     * List<UserDto>
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllUser() {
        return new ResponseEntity<>(new ApiResponse("Success", userService.getAllUser()),
                HttpStatus.OK);
    }

    /**
     * Lấy thông tin 1 người dùng by id
     * ADMIN - USER
     * UserDto
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new ApiResponse("Success",
                userService.getUserById(id)), HttpStatus.OK);
    }



    /**
     * Update avatar
     * USER - ADMIN
     * UserDto
     */
    @PutMapping("/avatar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateAvatar(@RequestParam("id") Long id,
                                        @RequestParam("avatar") MultipartFile avatar) {

        return new ResponseEntity<>(new ApiResponse( "Edit Success",
                userService.updateAvatar(id, avatar)), HttpStatus.OK);
    }

    /**
     * Update password
     * USER - ADMIN
     */
    @PutMapping("/password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request ) {

        return new ResponseEntity<>(new ApiResponse("Edit Success",
                userService.updatePassword(request)), HttpStatus.OK);
    }

//
//    /**
//     * Nguoi dung yeu thich bai hat
//     * USER
//     */
//    @PutMapping("/love")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    public ResponseEntity<?> loveUser(@RequestParam("userId") Long userId, @RequestParam("songId") Long songId) {
//        userService.loveSong(userId, songId);
//        return new ResponseEntity<>(new ApiResponse(true, "Success", ""), HttpStatus.OK);
//    }
//
//    /**
//     * Nguoi dung bo yeu thich bai hat
//     * USER
//     */
//    @PutMapping("/unlove")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    public ResponseEntity<?> unloveSong(@RequestParam("userId") Long userId, @RequestParam("songId") Long songId) {
//        userService.unloveSong(userId, songId);
//
//        return new ResponseEntity<>(new ApiResponse(true, "Success", ""), HttpStatus.OK);
//    }
//
//    /**
//     * Lấy ds love song cua nguoi dung
//     * USER
//     */
//    @GetMapping("/{id}/get-love-song")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    public ResponseEntity<?> getLoveSong(@PathVariable Long id) {
//        Set<Song> songs = userService.getLoveSong(id);
//        Set<SongResponse> responses = SongResponse.toResponseSet(songs);
//        return new ResponseEntity<>(new ApiResponse(true, "Success", responses), HttpStatus.OK);
//    }
//
//    /**
//     * Kiem tra co yeu thich ko
//     * USER
//     */
//    @GetMapping("/{id}/is-love-song/{songId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
//    public ResponseEntity<?> isLoveSong(@PathVariable Long id, @PathVariable Long songId) {
//        boolean b = userService.isLoveSong(id, songId);
//        return new ResponseEntity<>(new ApiResponse(true, "Success", b), HttpStatus.OK);
//    }

}
