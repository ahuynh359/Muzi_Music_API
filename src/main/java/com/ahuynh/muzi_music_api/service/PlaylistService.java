package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.PlaylistDto;
import com.ahuynh.muzi_music_api.model.entity.Playlist;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.mapper.PlaylistMapper;
import com.ahuynh.muzi_music_api.model.mapper.UserMapper;
import com.ahuynh.muzi_music_api.payload.request.AddPlaylistRequest;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PlaylistMapper playlistMapper;

    public PlaylistDto createPlaylist(AddPlaylistRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("User not found" + request.getUserId()));
        Playlist playlist = new Playlist(request.getName(), user);
        return playlistMapper.convertToDto(playlistRepository.save(playlist));

    }

    public Set<PlaylistDto> getAllPlaylist(CustomUserDetail currentUser) {
        User user = userRepository.getUser(currentUser);
        return playlistMapper.convertToDtoSet(playlistRepository.findAllByUserId(user.getId()));
    }
}
