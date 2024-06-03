package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.SongRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepository albumRepository;
    private final FirebaseService firebaseService;

    public Song save( String name,MultipartFile avatar,MultipartFile file,String lyrics, Long albumIb) {
        Album album = albumRepository.findById(albumIb).orElseThrow(() -> new ResourceNotFoundException("No Album with " + albumIb));
        String urlAvatar = firebaseService.upload(avatar,"image/png");
        String urlFile = firebaseService.upload(file,"audio/mpeg");
        Song s = new Song(name,urlAvatar,urlFile,lyrics,album);

        return songRepository.save(s);
    }

    public Song getSong(Long id) {
        return songRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Song with id " + id + " not found"));
    }

    public void deleteSong(Long id) {
        songRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Song with id " + id + " not found"));
        songRepository.deleteById(id);
    }

    public Song updateSong(Long id, SongRequest newSong) {
        Song updatedSong = songRepository.findSongById(id).orElseThrow(()
                -> new ResourceNotFoundException("Song not exits id =" + id.toString()));

        Album updatedAlbum =
                albumRepository.findAlbumById(newSong.getAlbumId()).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + newSong.getAlbumId().toString()));

        if (newSong.getName() != null) {
            updatedSong.setName(newSong.getName());
        }
        if (newSong.getAlbumId() != null) {
            updatedSong.setAlbum(updatedAlbum);
        }

        return songRepository.save(updatedSong);
    }
}
