package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CurrentUser;
import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.*;
import com.ahuynh.muzi_music_api.model.mapper.AlbumMapper;
import com.ahuynh.muzi_music_api.model.mapper.SingerMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.payload.response.LoveSongResponse;
import com.ahuynh.muzi_music_api.payload.response.SearchResponse;
import com.ahuynh.muzi_music_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final ListenRepository listenRepository;


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
        Song song = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song not found " + id));
        songRepository.deleteById(id);
    }

    public List<SongDto> getAllSong() {
        return songMapper.convertToDtoList(songRepository.findAll());
    }

    public List<SongDto> getNewSongs() {
        return songMapper.convertToDtoList(songRepository.findTop4ByOrderByCreatedAtDesc());
    }

    public List<SongDto> getTop10Songs() {
        List<Listen> listens = listenRepository.findAll();

        Map<Song, Long> songListenCounts = listens.stream()
                .collect(Collectors.groupingBy(Listen::getSong, Collectors.counting()));

        List<Song> songs =  songListenCounts.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return songMapper.convertToDtoList(songs);
    }


    public SearchResponse search(String query) {
        List<SongDto> songs = songMapper.convertToDtoList(songRepository.findByNameContainingIgnoreCase(query));
        List<AlbumDto> albums = albumMapper.convertToDtoList(albumRepository.findByNameContainingIgnoreCase(query));
        List<SingerDto> singers = singerMapper.convertToDtoList(singerRepository.findByNameContainingIgnoreCase(query));
        return new SearchResponse(songs, albums, singers);
    }

    public void loveOrUnloveSong(Long songId, CustomUserDetail currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        Song song = songRepository.findById(songId).orElseThrow(() -> new EntityNotFoundException("Song not found " + songId));
        if (user.getLoveSongs().contains(song))
            user.removeLoveSong(song);
        else user.addLovedSong(song);
        userRepository.save(user);
    }


    public LoveSongResponse getLoveSongs(CustomUserDetail currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        Set<Song> loveSongs = userRepository.findLoveSongById(user.getId());
        return new LoveSongResponse(loveSongs.size() + " songs ", songMapper.convertToDtoList(loveSongs));

    }

    public boolean isUserLoveSong(CustomUserDetail currentUser, Long songId) {

        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        Song song = songRepository.findById(songId).orElseThrow(() -> new EntityNotFoundException("Song not found " + songId));
        return user.getLoveSongs().contains(song);
    }


    public void listenToSong(CustomUserDetail currentUser, Long songId) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("User not found " + currentUser.getId()));
        Song song = songRepository.findById(songId).orElseThrow(() -> new RuntimeException("Song not found " + songId));

        Listen listen = new Listen(user,song);

        listenRepository.save(listen);
    }
}

