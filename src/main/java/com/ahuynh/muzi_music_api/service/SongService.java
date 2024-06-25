package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.Album;
import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.Type;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.request.AddSongRequest;
import com.ahuynh.muzi_music_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final FirebaseService firebaseService;
    private final SongMapper songMapper;
    private final SingerRepository singerRepository;
    private final TypeRepository typeRepository;


    public SongDto createSong(String name, MultipartFile avatar, MultipartFile file, String lyrics, Long albumIb, Set<Long> singerId, Set<Long> typeId) {
        Album album = albumRepository.findById(albumIb).orElseThrow(() -> new EntityNotFoundException("No Album with " + albumIb));
        String urlAvatar = firebaseService.upload(avatar, "image/png");
        String urlFile = firebaseService.upload(file, "audio/mpeg");
        Set<Singer> singers = new HashSet<>();
        for (Long id : singerId) {
            Singer singer = singerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Singer not found " + id));
            singers.add(singer);
        }
        Set<Type> types = new HashSet<>();
        for (Long id : typeId) {
            Type type = typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Type not found " + id));
            types.add(type);
        }
        Song s = new Song(name, urlAvatar, urlFile, lyrics, album, singers, types);
        return songMapper.convertToDto(songRepository.save(s));
    }

    public SongDto getSongById(Long id) {
        return songMapper.convertToDto(songRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Song with id " + id + " not found")));

    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public List<SongDto> getAllSong() {
        return songMapper.convertToDtoList(songRepository.findAll());
    }

    public List<SongDto> getNewSongs() {
        return songMapper.convertToDtoList(songRepository.findTop10ByOrderByCreatedAtDesc());
    }


}

