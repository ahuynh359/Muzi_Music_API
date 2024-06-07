package com.ahuynh.muzi_music_api.service;


import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.payload.request.PlaylistRequest;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import jdk.jfr.Timestamp;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;


    public Playlist getPlaylistByIdAndUserId(Long id,Long userId) {
        return playlistRepository.findByIdAndUserId(id,userId).orElseThrow(() ->
                new ResourceNotFoundException("Playlist with id " + id + " not found"));
    }
    @Transactional
    public void deletePlaylist(Long id) {
        playlistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Playlist not exits id =" + id));
        playlistRepository.deleteById(id);
    }

    public Playlist updatePlaylist(Long iid, PlaylistRequest newPlaylist) {
        Playlist updatedPlaylist = playlistRepository.findById(iid).orElseThrow(() ->
                new ResourceNotFoundException("Playlist not exits id =" + iid));
        if (playlistRepository.existsByName(newPlaylist.getName())) {
            throw new DuplicateException("Playlist with name " + newPlaylist.getName() + " already exists");
        }

        if (newPlaylist.getName() != null) {
            updatedPlaylist.setName(newPlaylist.getName());
        }


        return playlistRepository.save(updatedPlaylist);
    }


    public void addPlaylistByUser(PlaylistRequest playlistRequest) {
        User user = userRepository.findById(playlistRequest.getUserId()).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + playlistRequest.getUserId() + " not found"));
        if (playlistRepository.existsByName(playlistRequest.getName())
                && playlistRepository.existsByUserId(playlistRequest.getUserId())) {
            throw new DuplicateException("Playlist with name " + playlistRequest.getName() + " already exists with user id " + playlistRequest.getUserId());
        }
     ;

        Playlist playlist = new Playlist(playlistRequest.getName(), user);
        user.addPlaylist(playlist);

        userRepository.save(user);
    }

    public List<Playlist> getAllPlaylistByUser(Long userId) {
        return playlistRepository.findAllByUserId(userId);
    }


    public Playlist getPlaylistByNameAndUser(String name,Long userId) {
        return playlistRepository.findByNameAndUserId(name,userId).orElseThrow(() ->
                new ResourceNotFoundException("Playlist with name " + name + " not found"));
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Playlist with id " + id + " not found"));
    }

    public void addSongToPlaylist(Long songId, Long playlistId) {
        Playlist updatedPlaylist = playlistRepository.findById(playlistId).orElseThrow(() ->
                new ResourceNotFoundException("Playlist not exits id = " + playlistId));


        Song updateSong = songRepository.findById(songId).orElseThrow(() ->
                new ResourceNotFoundException("Song not exits id = " + playlistId));

        updatedPlaylist.addSong(updateSong);
        songRepository.save(updateSong);
    }

    public void deleteSongFromPlaylist(Long songId, Long playlistId) {
        Playlist updatedPlaylist = playlistRepository.findById(playlistId).orElseThrow(() ->
                new ResourceNotFoundException("Playlist not exits id = " + playlistId));


        Song updateSong = songRepository.findById(songId).orElseThrow(() ->
                new ResourceNotFoundException("Song not exits id = " + playlistId));

        updatedPlaylist.removeSong(updateSong);
        songRepository.save(updateSong);
    }

    public List<Song> getAllSongFromPlaylist(Long playlistId) {
        Playlist updatedPlaylist = playlistRepository.findById(playlistId).orElseThrow(() ->
                new ResourceNotFoundException("Playlist not exits id = " + playlistId));

        return updatedPlaylist.getSongs();
    }
}