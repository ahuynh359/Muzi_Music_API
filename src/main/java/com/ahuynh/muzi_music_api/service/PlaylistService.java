package com.ahuynh.muzi_music_api.service;


import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Playlist;
import com.ahuynh.muzi_music_api.payload.request.PlaylistRequest;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import com.ahuynh.muzi_music_api.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository PlaylistRepository;
    private final ModelMapper modelMapper;



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
        if(PlaylistRepository.existsByName(newPlaylist.getName())){
            throw new DuplicateException("Playlist with name " + newPlaylist.getName() + " already exists");
        }

        if(newPlaylist.getName() != null){
            updatedPlaylist.setName(newPlaylist.getName());
        }


        return PlaylistRepository.save(updatedPlaylist);
    }


    public Playlist save(PlaylistRequest playlistRequest) {
        if(PlaylistRepository.existsByName(playlistRequest.getName())){
            throw new DuplicateException("Playlist with name " + playlistRequest.getName() + " already exists");
        }
        Playlist Playlist = new Playlist(playlistRequest.getName());

        return PlaylistRepository.save(Playlist);
    }

}