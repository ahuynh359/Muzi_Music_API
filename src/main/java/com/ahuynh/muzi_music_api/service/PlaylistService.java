package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.PlaylistDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.Playlist;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.mapper.PlaylistMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.model.mapper.UserMapper;
import com.ahuynh.muzi_music_api.payload.request.AddPlaylistRequest;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PlaylistMapper playlistMapper;
    private final SongRepository songRepository;
    private final SongMapper songMapper;

    public PlaylistDto createPlaylist(AddPlaylistRequest request, CustomUserDetail currentUser) {
        User user = userRepository.getUser(currentUser);
        Playlist playlist = new Playlist(request.getName(), user);
        if (playlistRepository.existsByNameAndUserId(request.getName(), user.getId())) {
            throw new EntityExistsException("Playlist with name " + request.getName() + " already exists");
        }
        return playlistMapper.convertToDto(playlistRepository.save(playlist));

    }

    public Set<PlaylistDto> getAllPlaylist(CustomUserDetail currentUser) {
        User user = userRepository.getUser(currentUser);
        return playlistMapper.convertToDtoSet(playlistRepository.findAllByUserId(user.getId()));
    }

    public void deletePlaylist(Long id, CustomUserDetail currentUser) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
        User user = userRepository.getUser(currentUser);
        if (playlist.getUser().getId().equals(user.getId())) {
            playlistRepository.delete(playlist);
        } else
            throw new CustomException("You are not allowed to delete this playlist");
    }

    public PlaylistDto updatePlaylist(AddPlaylistRequest request, CustomUserDetail currentUser, Long id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
        User user = userRepository.getUser(currentUser);
        if (playlist.getUser().getId().equals(user.getId())) {
            if (playlistRepository.existsByNameAndUserId(request.getName(), user.getId())) {
                throw new EntityExistsException("Playlist with name " + request.getName() + " already exists");
            }
            playlist.setName(request.getName());
            playlistRepository.save(playlist);
            return playlistMapper.convertToDto(playlist);
        }
        throw new CustomException("You are not allowed to update this playlist");
    }

    public void addSongToPlaylist(Long playlistId, Long songId, CustomUserDetail currentUser) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new EntityNotFoundException("Playlist with id " + playlistId + " not found"));
        Song song = songRepository.findById(songId).orElseThrow(() -> new EntityNotFoundException("Song with id " + songId + " not found"));
        User user = userRepository.getUser(currentUser);
        if (playlist.getUser().getId().equals(user.getId())) {
            playlist.addSong(song);
            playlistRepository.save(playlist);
            return;
        }
        throw new CustomException("You are not allowed to update this playlist");
    }

    public Set<SongDto> getAllSongsOfPlaylist(CustomUserDetail currentUser, Long id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
        User user = userRepository.getUser(currentUser);
        if (playlist.getUser().getId().equals(user.getId())) {
           return songMapper.convertToDtoSet(playlist.getSongs());
        }
        throw new CustomException("You are not allowed to update this playlist");
    }

    public Set<SongDto> getAllSongsNotFromPlaylist(CustomUserDetail currentUser, Long id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Playlist with id " + id + " not found"));
        User user = userRepository.getUser(currentUser);
        if (playlist.getUser().getId().equals(user.getId())) {
            List<Song> songsInPlaylist = playlistRepository.findSongsById(id);
            List<Song> allSongs = songRepository.findAll();
            List<Song> notInPlaylist = new ArrayList<>();
            for(Song s : allSongs){
                if(!songsInPlaylist.contains(s)){
                    notInPlaylist.add(s);
                }
            }
            return songMapper.convertToDtoSet(notInPlaylist);

        }
        throw new CustomException("You are not allowed to update this playlist");
    }
}
