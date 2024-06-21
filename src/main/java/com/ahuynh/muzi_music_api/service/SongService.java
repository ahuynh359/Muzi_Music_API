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

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final UserRepository userRepository;
    private final AlbumRepository albumRepository;
    private final FirebaseService firebaseService;
    private final SongMapper songMapper;
    private final SingerRepository singerRepository;
    private final TypeRepository typeRepository;


    public SongDto createSong(String name, MultipartFile avatar, MultipartFile file, String lyrics, Long albumIb, Set<Long> singerId, Set<Long> typeId) {
        Album album = albumRepository.findById(albumIb).orElseThrow(() -> new EntityNotFoundException("No Album with " + albumIb));
        String urlAvatar = firebaseService.upload(avatar, "image/png");
        String urlFile = firebaseService.upload(file, "audio/mpeg");
        Song s = new Song(name, urlAvatar, urlFile, lyrics, album);
        for (Long id : singerId) {
            Singer singer = singerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Singer not found " + id));
            s.addSinger(singer);
        }

        for (Long id : typeId) {
            Type type = typeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Type not found " + id));
            s.addType(type);
        }
        return songMapper.convertToDto(songRepository.save(s));
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Song with id " + id + " not found"));
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

//    public Song updateSong(Long id, UpdateSongRequest newSong) {
//        Song updatedSong = songRepository.findSongById(id).orElseThrow(()
//                -> new ResourceNotFoundException("Song not exits id =" + id.toString()));
//
//        Album updatedAlbum =
//                albumRepository.findById(newSong.getAlbumId()).orElseThrow(() -> new ResourceNotFoundException("Album not exits id =" + newSong.getAlbumId().toString()));
//
//        if (newSong.getName() != null) {
//            updatedSong.setName(newSong.getName());
//        }
//        if (newSong.getAlbumId() != null) {
//            updatedSong.setAlbum(updatedAlbum);
//        }
//        if (newSong.getLyrics() != null) {
//            updatedSong.setLyrics(newSong.getLyrics());
//        }
//        if (newSong.getSinger() != null) {
//            updatedSong.setLyrics(newSong.getSinger());
//        }
//
//        return songRepository.save(updatedSong);
//    }
//
    public List<Song> getAllSong() {
        return songRepository.findAll();
    }

//
//    public Song updateSongLove(Long userId, Long songId, int love) {
//        Song updateSong = songRepository.findById(songId).orElseThrow(() ->
//                new ResourceNotFoundException("Song with id " + songId + " not found"));
//        User updateUser = userRepository.findById(userId).orElseThrow(() ->
//                new ResourceNotFoundException("User with id " + userId + " not found"));
//
//        if (love == 1)
//            updateUser.addLovedSong(updateSong);
//        else updateUser.removeLovedSong(updateSong);
//        return songRepository.save(updateSong);
//
//
//    }
//
//    public List<Type> getTypeOfSong(Long id) {
//        songRepository.findById(id).orElseThrow(() ->
//                new ResourceNotFoundException("Song with id " + id + " not found"));
//        return songRepository.findAllTypeById(id).orElseThrow(()
//                -> new ResourceNotFoundException("There is no type in this song " + id.toString()));
//    }
//

}

