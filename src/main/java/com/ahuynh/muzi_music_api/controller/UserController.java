package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.payload.request.UserRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.SongResponse;
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
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.CREATED);
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
    public ResponseEntity<?> editUserWithoutAvatar(@PathVariable(name = "id") Long id,
                                                   @RequestBody UserRequest userRequest) {
        User user = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }


    /**
     * Mở khóa người dùng
     * ADMIN
     */
    @PutMapping("/enable")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> unlock(@RequestParam(name = "id") Long id) {
        User user = userService.updateEnable(id, true);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully", objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }

    /**
     * Khóa người dùng
     * ADMIN
     */
    @PutMapping("/disable")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> lock(@RequestParam(name = "id") Long id) {
        User user = userService.updateEnable(id, false);
        return new ResponseEntity<>(new ApiResponse(true, "Update Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }

    /**
     * Lấy thông tin tất cả người dùng
     * ADMIN - USER
     */
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllUser() {
        List<User> user = userService.getAllUser();
        List<UserResponse> response = UserResponse.toResponseList(user);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", response),
                HttpStatus.OK);
    }

    /**
     * Lấy thông tin 1 người dùng by id
     * ADMIN - USER
     * UserResponse
     */
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }


    /**
     * Lấy thông tin 1 người dùng theo username
     * ADMIN - USER
     * UserResponse
     */
    @GetMapping("/username")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserByUserName(@RequestParam("username") String username) {
        User user = userService.getUserByUserName(username);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }

    /**
     * Lấy thông tin 1 người dùng theo gmail
     * ADMIN - USER
     * UserResponse
     */
    @GetMapping("/email")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserByEmail(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }

    /**
     * Người dùng follow người dùng
     * USER
     */
    @PostMapping("/follow")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> followUser(@RequestParam("followerId") Long followerId,
                                        @RequestParam("followedId") Long followedId) {

        userService.followUser(followerId, followedId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""),
                HttpStatus.OK);
    }

    /**
     * Người dùng unfollow người dùng
     * USER
     */
    @PostMapping("/unfollow")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> unfollowUser(@RequestParam("followerId") Long followerId,
                                          @RequestParam("followedId") Long followedId) {

        userService.unfollowUser(followerId, followedId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""),
                HttpStatus.OK);
    }

    /**
     * Lấy ds một người dùng đang theo dõi ai
     * USER
     */
    @GetMapping("/getFollowing")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getFollowing(@RequestParam("id") Long id) {
        List<User> following = userService.getFollowing(id);
        List<UserResponse> responses = UserResponse.toResponseList(following);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully",
                responses), HttpStatus.OK);
    }

    /**
     * Lấy ds một người đang có ai theo dõi
     * USER
     */
    @GetMapping("/getFollower")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getFollower(@RequestParam("id") Long id) {
        List<User> follower = userService.getFollower(id);
        List<UserResponse> responses = UserResponse.toResponseList(follower);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", responses), HttpStatus.OK);
    }



    /**
     * Edit avatar
     * USER - ADMIN
     */
    @PutMapping("/editAvatar")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> editAvatar(@RequestParam("id") Long id,
                                        @RequestParam("avatar") MultipartFile avatar) {

        User user = userService.editAvatar(id, avatar);
        return new ResponseEntity<>(new ApiResponse(true, "Edit Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }

    /**
     * Edit password
     * USER - ADMIN
     */
    @PutMapping("/editPassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> editPassword(@RequestParam("id") Long id,
                                          @RequestParam("password") String password) {

        User user = userService.editPassword(id, password);
        return new ResponseEntity<>(new ApiResponse(true, "Edit Successfully",
                objectMapper.convertValue(user, UserResponse.class)), HttpStatus.OK);
    }


    /**
     * Nguoi dung yeu thich bai hat
     * USER
     *
     */
    @PutMapping("/love")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> loveUser(@RequestParam("userId") Long userId, @RequestParam("songId")  Long songId) {
        userService.loveSong(userId, songId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""), HttpStatus.OK);
    }

    /**
     * Nguoi dung bo yeu thich bai hat
     * USER
     */
    @PutMapping("/unlove")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> unloveSong(@RequestParam("userId") Long userId, @RequestParam("songId")  Long songId) {
        userService.unloveSong(userId, songId);

        return new ResponseEntity<>(new ApiResponse(true, "Successfully", ""), HttpStatus.OK);
    }

    /**
     * Lấy ds love song cua nguoi dung
     * USER
     */
    @GetMapping("/{id}/get-love-song")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> getLoveSong(@PathVariable Long id) {
        Set<Song> songs = userService.getLoveSong(id);
        Set<SongResponse> responses = SongResponse.toResponseSet(songs);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", responses), HttpStatus.OK);
    }

    /**
     * Kiem tra co yeu thich ko
     * USER
     */
    @GetMapping("/{id}/is-love-song/{songId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> isLoveSong(@PathVariable Long id, @PathVariable Long songId) {
        boolean b = userService.isLoveSong(id, songId);
        return new ResponseEntity<>(new ApiResponse(true, "Successfully", b), HttpStatus.OK);
    }

}
