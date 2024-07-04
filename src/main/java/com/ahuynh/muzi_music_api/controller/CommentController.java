package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.payload.request.CommentRequest;
import com.ahuynh.muzi_music_api.payload.request.EditCommentRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageResponse;
import com.ahuynh.muzi_music_api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * Get binh luan cua 1 bai hat
     * ADMIN - USER
     * List<CommentDto>
     */
    @GetMapping("/song/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') ")
    public ResponseEntity<?> getAllCommentBySongId(@PathVariable Long id) {

        return new ResponseEntity<>(new ApiResponse("Success",
                commentService.getAllCommentBySongId(id)), HttpStatus.OK);
    }



    /**
     * Them comment vao bai hat
     * ADMIN
     * CommentDto
     */
    @PostMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER') ")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest request) {
        return new ResponseEntity<>(new ApiResponse("Success", commentService.addComment(request)), HttpStatus.OK);
    }

    /**
     * Sua binh luan
     * ADMIN - USER
     * CommentDto
     */
    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')  or hasRole('ROLE_USER')")
    public ResponseEntity<?> editComment(@RequestBody EditCommentRequest request) {
        return new ResponseEntity<>(new ApiResponse(" Success",
                commentService.editComment(request)), HttpStatus.OK);
    }

    /**
     * xoa binh luan
     * ADMIN - USER
     * Message
     */
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')  or hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(new MessageResponse(" Success"), HttpStatus.OK);
    }

}
