package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.*;
import com.ahuynh.muzi_music_api.model.mapper.AlbumMapper;
import com.ahuynh.muzi_music_api.model.mapper.SingerMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.request.AddSongRequest;
import com.ahuynh.muzi_music_api.payload.response.SearchResponse;
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
    private final AlbumMapper albumMapper;
    private final SingerMapper singerMapper;
    private final UserRepository userRepository;


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


    public SearchResponse search(String query) {
        List<SongDto> songs = songMapper.convertToDtoList(songRepository.findByNameContainingIgnoreCase(query));
        List<AlbumDto> albums = albumMapper.convertToDtoList(albumRepository.findByNameContainingIgnoreCase(query));
        List<SingerDto> singers = singerMapper.convertToDtoList(singerRepository.findByNameContainingIgnoreCase(query));
        return new SearchResponse(songs, albums, singers);
    }

    public void loveOrUnloveSong(Long userId, Long songId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
        Song song = songRepository.findById(songId).orElseThrow(() -> new EntityNotFoundException("Song not found " + songId));
        if (user.getLoveSongs().contains(song))
            user.removeLovedSong(song);
        else user.addLovedSong(song);
        userRepository.save(user);
    }


    public Set<SongDto> getLoveSongByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
        return songMapper.convertToDtoSet(user.getLoveSongs());

    }

    public boolean isUserLoveSong(Long userId, Long songId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found " + userId));
        Song song = songRepository.findById(songId).orElseThrow(() -> new EntityNotFoundException("Song not found " + songId));
        return user.getLoveSongs().contains(song);
    }
}

