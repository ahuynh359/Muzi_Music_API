package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.mapper.CommentMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.request.CommentRequest;
import com.ahuynh.muzi_music_api.payload.request.EditCommentRequest;
import com.ahuynh.muzi_music_api.payload.response.CommentParentAndChildrenResponse;
import com.ahuynh.muzi_music_api.repository.CommentRepository;
import com.ahuynh.muzi_music_api.repository.SingerRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public List<CommentDto> getAllCommentBySongId(Long id) {
        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No song found"));
        return commentMapper.convertToDtoList(song.getComments());
    }



    public CommentDto addComment(CommentRequest request) {
        Song song = songRepository.findById(request.getSongId()).orElseThrow(() -> new EntityNotFoundException("No song found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("No user found"));
        Comment comment = new Comment(user, song, request.getContent());
        return commentMapper.convertToDto(commentRepository.save(comment));

    }



    public CommentDto editComment(EditCommentRequest request) {
        Song song = songRepository.findById(request.getSongId()).orElseThrow(() -> new EntityNotFoundException("No song found"));
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("No user found"));
        if (!Objects.equals(request.getUserId(), user.getId())) {
            throw new EntityNotFoundException("Don't allow to edit comment");
        }
        Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow(() -> new EntityNotFoundException("No comment found"));
        comment.setContent(request.getContent());

        return commentMapper.convertToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
