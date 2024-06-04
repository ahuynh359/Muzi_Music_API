package com.ahuynh.muzi_music_api.service;


import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.payload.request.PlaylistRequest;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository PlaylistRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;


    public Playlist getPlaylist(Long id) {
        return PlaylistRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Playlist with id " + id + " not found"));
    }

    public void deletePlaylist(Long id) {
        PlaylistRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Playlist not exits id =" + id));
        PlaylistRepository.deleteById(id);
    }

    public Playlist updatePlaylist(Long iid, PlaylistRequest newPlaylist) {
        Playlist updatedPlaylist = PlaylistRepository.findById(iid).orElseThrow(() ->
                new ResourceNotFoundException("Playlist not exits id =" + iid));
        if (PlaylistRepository.existsByName(newPlaylist.getName())) {
            throw new DuplicateException("Playlist with name " + newPlaylist.getName() + " already exists");
        }

        if (newPlaylist.getName() != null) {
            updatedPlaylist.setName(newPlaylist.getName());
        }


        return PlaylistRepository.save(updatedPlaylist);
    }


    public Playlist save(PlaylistRequest playlistRequest) {
        if (PlaylistRepository.existsByName(playlistRequest.getName())) {
            throw new DuplicateException("Playlist with name " + playlistRequest.getName() + " already exists");
        }
        User user = userRepository.findById(playlistRequest.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User with id " + playlistRequest.getId() + " not found"));

        Playlist Playlist = new Playlist(playlistRequest.getName(), user);

        return PlaylistRepository.save(Playlist);
    }

    public List<Playlist> getAllPlaylist() {
        return playlistRepository.findAll();
    }
}