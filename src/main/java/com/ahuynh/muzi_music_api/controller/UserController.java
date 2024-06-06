package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.payload.request.UserRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.UserResponse;
import com.ahuynh.muzi_music_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    /**
     * Thêm người dùng chua lam xac thuc otp cho nay
     * ADMIN
     */
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addUser(@RequestParam("email") String email,
                                     @RequestParam("password") String password,
                                     @RequestParam("username") String username,
                                     @RequestParam("avatar") MultipartFile avatar,
                                     @RequestParam("enable") boolean enable) {

        User user = userService.save(email, password, username, avatar, enable);
        return new ResponseEntity<>(new ApiResponse(true, "Create Successfully",
                user), HttpStatus.CREATED);
    }

    /**
     * Xóa người dùng
     * ADMIN
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse(true, "Delete Successfully",
                ""), HttpStatus.OK);
    }

    /**
     * Sửa người dùng - ko có avatar
     * ADMIN, USER
     */
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id,
                                        @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",
                user), HttpStatus.OK);
    }


    /**
     * Mở khóa người dùng
     * ADMIN
     */
    @PutMapping("/enable/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> unlock(@PathVariable(name = "id") Long id) {
        User user = userService.updateEnable(id, true);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully", objectMapper.convertValue(user, User.class)), HttpStatus.OK);
    }

    /**
     * Khóa người dùng
     * ADMIN
     */
    @PutMapping("/disable/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> lock(@PathVariable(name = "id") Long id) {
        User user = userService.updateEnable(id, false);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",
                user), HttpStatus.OK);
    }

    /**
     * Lấy thông tin tất cả người dùng
     * ADMIN - USER
     */
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllUser() {
        List<User> user = userService.getAllUser();
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", user),
                HttpStatus.OK);
    }

    /**
     * Lấy thông tin 1 người dùng
     * ADMIN - USER
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                user), HttpStatus.OK);
    }


    /**
     * Lấy thông tin 1 người dùng theo username
     * ADMIN - USER
     */
    @GetMapping("/byUserName/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserByUserName(@PathVariable String username) {
        User user = userService.getUserByUserName(username);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                user), HttpStatus.OK);
    }

    /**
     * Người dùng follow người dùng
     * USER
     */
    @PostMapping("/{followerId}/follow/{followedId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> followUser(@PathVariable Long followerId, @PathVariable Long followedId) {

        userService.followUser(followerId, followedId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""),
                HttpStatus.OK);
    }

    /**
     * Người dùng unfollow người dùng
     * USER
     */
    @PostMapping("/{followerId}/unfollow/{followedId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followerId, @PathVariable Long followedId) {

        userService.unfollowUser(followerId, followedId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""),
                HttpStatus.OK);
    }

    /**
     * Lấy ds một người dùng đang theo dõi ai
     * USER
     */
    @GetMapping("/{id}/following")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getFollowing(@PathVariable Long id) {
        List<User> following = userService.getFollowing(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                following), HttpStatus.OK);
    }

    /**
     * Lấy ds một người đang có ai theo dõi
     * USER
     */
    @GetMapping("/{id}/follower")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getFollower(@PathVariable Long id) {
        List<User> follower = userService.getFollower(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", follower), HttpStatus.OK);
    }

    /**
     * Nguoi dung yeu thich bai hat
     * USER
     */
    @PutMapping("/{userId}/love/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> loveUser(@PathVariable Long userId, @PathVariable Long songId) {
        userService.loveSong(userId, songId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""), HttpStatus.OK);
    }

    /**
     * Nguoi dung bo yeu thich bai hat
     * USER
     */
    @PutMapping("/{userId}/unlove/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> unloveSong(@PathVariable Long userId, @PathVariable Long songId) {
        userService.unloveSong(userId, songId);

        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""), HttpStatus.OK);
    }

    /**
     * Lấy ds love song cua nguoi udng
     * USER
     */
    @GetMapping("/{id}/loveSong")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getLoveSong(@PathVariable Long id) {
        Set<Song> songs = userService.getLoveSong(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", songs), HttpStatus.OK);
    }

    /**
     * Lấy ds love song cua nguoi udng
     * USER
     */
    @GetMapping("/{id}/isLoveSong/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> isLoveSong(@PathVariable Long id, @PathVariable Long songId) {
        boolean b = userService.isLoveSong(id, songId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", b), HttpStatus.OK);
    }

    /**
     * Edit avatar
     * USER - ADMIN
     */
    @PostMapping("/{id}/editAvatar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> editAvatar(@PathVariable Long id, @RequestParam("avatar") MultipartFile avatar) {

        User user = userService.editAvatar(id , avatar);
        return new ResponseEntity<>(new ApiResponse(true, "Edit Successfully",
                user), HttpStatus.CREATED);
    }

    /**
     * Lấy ds love song cua nguoi udng
     * USER
     */
    @GetMapping("/{id}/playlist")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllPlaylistById(@PathVariable Long id) {
        List<Playlist> playlist = userService.getAllPlaylistById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", playlist), HttpStatus.OK);
    }



}
