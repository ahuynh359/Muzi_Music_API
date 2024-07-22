package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import com.ahuynh.muzi_music_api.model.dto.UserDto;
import com.ahuynh.muzi_music_api.model.entity.*;
import com.ahuynh.muzi_music_api.model.entity.role.RoleName;
import com.ahuynh.muzi_music_api.model.mapper.CommentMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.request.CommentRequest;
import com.ahuynh.muzi_music_api.payload.request.UpdateCommentRequest;
import com.ahuynh.muzi_music_api.payload.response.CommentResponse;
import com.ahuynh.muzi_music_api.repository.CommentRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.utils.SortName;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentResponse getAllCommentBySongId(Long id, CustomUserDetail currentUser) {


        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));


        Set<CommentDto> commentsDto = commentMapper.convertToDtoSet(song.getComments());

        return new CommentResponse(commentsDto, commentsDto.size());
    }
    public List<CommentDto> getAllComments(SortName sort) {
        List<Comment> comments = new ArrayList<>();
        switch (sort) {
            case A_Z -> {
                comments = commentRepository.findAllByOrderByContentAsc();
            }
            case Z_A -> {
                comments = commentRepository.findAllByOrderByContentDesc();
            }
            case NEW -> {
                comments = commentRepository.findAllByOrderByCreatedAtDesc();
            }
            case OLD -> {
                comments = commentRepository.findAllByOrderByCreatedAtAsc();
            }

            default -> commentRepository.findAllByOrderByCreatedAtDesc();

        }
        return commentMapper.convertToDtoList(comments);
    }


    public CommentDto addComment(CommentRequest request, CustomUserDetail currentUser) {
        Song song = songRepository.findById(request.getSongId()).orElseThrow(() -> new EntityNotFoundException("No song found"));
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("No user found"));
        Comment comment = new Comment(user, song, request.getContent());
        return commentMapper.convertToDto(commentRepository.save(comment));

    }


    public CommentDto editComment(UpdateCommentRequest request, CustomUserDetail currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("No user found"));
        if (!Objects.equals(currentUser.getId(), user.getId())) {
            throw new EntityNotFoundException("Don't allow to edit comment");
        }
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new EntityNotFoundException("No comment found"));
        comment.setContent(request.getContent());

        return commentMapper.convertToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id, CustomUserDetail currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("No user found"));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No comment found"));
        if (user.getRole().getName().equals(RoleName.ROLE_ADMIN)) {
            commentRepository.delete(comment);
            return;
        }
        if (!Objects.equals(user.getId(), comment.getUser().getId())) {
            throw new EntityNotFoundException("Don't allow to delete comment");
        }

        commentRepository.delete(comment);

    }





}
