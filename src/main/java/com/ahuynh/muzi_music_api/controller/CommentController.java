package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.payload.request.CommentRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateCommentRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.CommentService;
import com.ahuynh.muzi_music_api.utils.SortName;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/song/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> getAllCommentBySongId(@PathVariable Long id,@CurrentUser CustomUserDetail currentUser) {

        return new ResponseEntity<>(new ApiResponse("Get All Comments Of Song Successfully", commentService.getAllCommentBySongId(id,currentUser)), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllComments(@RequestParam(value = "sort", required = false, defaultValue = "NEW") SortName sort) {

        return new ResponseEntity<>(new ApiResponse("Get All Comments Successfully", commentService.getAllComments(sort)), HttpStatus.OK);
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest request, @CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse("Add Comment To Song Successfully", commentService.addComment(request, currentUser)), HttpStatus.OK);
    }


    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')  or hasRole('ROLE_USER')")
    public ResponseEntity<?> editComment(@RequestBody UpdateCommentRequest request, @CurrentUser CustomUserDetail currentUser) {
        return new ResponseEntity<>(new ApiResponse(" Success", commentService.editComment(request, currentUser)), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')  or hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, @CurrentUser CustomUserDetail currentUser) {
        commentService.deleteComment(id, currentUser);
        return new ResponseEntity<>(new MessageResponse(" Success"), HttpStatus.OK);
    }

}
