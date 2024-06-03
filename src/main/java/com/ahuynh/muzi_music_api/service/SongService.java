package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.DuplicateException;
import com.ahuynh.muzi_music_api.exception.ResourceNotFoundException;
import com.ahuynh.muzi_music_api.model.Album;
import com.ahuynh.muzi_music_api.model.Song;
import com.ahuynh.muzi_music_api.payload.request.SongRequest;
import com.ahuynh.muzi_music_api.repository.AlbumRepository;
import com.ahuynh.muzi_music_api.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final ModelMapper modelMapper;
    private final AlbumRepository albumRepository;

    public Song save(SongRequest songRequest) {
        Song s = new Song();
        modelMapper.map(songRequest, s);
        return songRepository.save(s);
    }

    public Song getSong(Long id) {
        return songRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Song with id " + id + " not found"));
    }

    public void deleteSong(Long id) {
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
