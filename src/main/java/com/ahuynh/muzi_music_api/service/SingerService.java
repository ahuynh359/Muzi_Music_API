package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.config.security.CustomUserDetail;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.Playlist;
import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.model.entity.Song;
import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.mapper.SingerMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.model.mapper.UserMapper;
import com.ahuynh.muzi_music_api.repository.SingerRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.utils.SortName;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/singer")
@RequiredArgsConstructor
public class SingerService {

    private final SingerRepository singerRepository;
    private final FirebaseService firebaseService;
    private final SingerMapper singerMapper;
    private final SongMapper songMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public SingerDto createSinger(String name, MultipartFile avatar) {
        String url = firebaseService.upload(avatar, "image/png");
        return singerMapper.convertToDto(singerRepository.save(new Singer(name, url)));
    }

    public SingerDto getSingerById(Long id) {
        return singerMapper.convertToDto(singerRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Singer with id " + id + " not found")));
    }

    public void deleteSinger(Long id) {
        Singer singer = singerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Singer with id " + id + " not found"));
        singerRepository.delete(singer);
    }

    public SingerDto updateSinger(Long id, String name, MultipartFile avatar) {
        Singer singer = singerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Singer with id " + id + " not found"));
        if (name != null) {
            singer.setName(name);
        }
        if (avatar != null) {
            String url = firebaseService.upload(avatar, "image/png");
            singer.setAvatar(url);
        }
        return singerMapper.convertToDto(singerRepository.save(singer));
    }

    public List<SingerDto> getAllSingers(SortName sort) {
        List<Singer> singers = new ArrayList<>();
        switch (sort) {
            case A_Z -> {
                singers = singerRepository.findAllByOrderByNameAsc();
            }
            case Z_A -> {
                singers = singerRepository.findAllByOrderByNameDesc();
            }
            case NEW -> {
                singers = singerRepository.findAllByOrderByCreatedAtDesc();
            }
            case OLD -> {
                singers = singerRepository.findAllByOrderByCreatedAtAsc();
            }

            default -> singerRepository.findAllByOrderByCreatedAtDesc();

        }

        return singerMapper.convertToDtoList(singers);
    }



    public List<SongDto> getSongsFromSinger(Long id) {

        return songMapper.convertToDtoList(singerRepository.findSongById(id));

    }


    public List<SingerDto> getLoveSingersOfUser(CustomUserDetail currentUser) {
        User user = userRepository.getUser(currentUser);
        return singerMapper.convertToDtoList(userRepository.findLoveSingerById(user.getId()));
    }

    public void loveOrUnloveSinger(Long id, CustomUserDetail currentUser) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        Singer singer = singerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Singer not found " + id));
        System.out.println(user.getLoveSingers());
        if (user.getLoveSingers().contains(singer)) {
            user.removeLoveSinger(singer);
        } else {
            user.addLoveSinger(singer);
        }
        userRepository.save(user);
    }

    public boolean isUserLoveSinger(CustomUserDetail currentUser, Long id) {

        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("User not found " + currentUser.getId()));
        Singer singer = singerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Singer not found " + id));
        return user.getLoveSingers().contains(singer);
    }
}
