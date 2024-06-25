package com.ahuynh.muzi_music_api.service;

import com.ahuynh.muzi_music_api.exception.EntityNotFoundException;
import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.entity.Singer;
import com.ahuynh.muzi_music_api.model.mapper.SingerMapper;
import com.ahuynh.muzi_music_api.model.mapper.SongMapper;
import com.ahuynh.muzi_music_api.repository.SingerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/singer")
@RequiredArgsConstructor
public class SingerService {

    private final SingerRepository singerRepository;
    private final FirebaseService firebaseService;
    private final SingerMapper singerMapper;
    private final SongMapper songMapper;

    public SingerDto createSinger(String name, MultipartFile avatar) {
        String url = firebaseService.upload(avatar, "image/png");
        return singerMapper.convertToDto(singerRepository.save(new Singer(name, url)));
    }

    public SingerDto getSingerById(Long id) {
        return singerMapper.convertToDto(singerRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Singer with id " + id + " not found")));
    }

    public void deleteSinger(Long id) {
        singerRepository.deleteById(id);
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

    public List<SingerDto> getAllSinger() {
        return singerMapper.convertToDtoList(singerRepository.findAll());
    }

    public List<SingerDto> getNewSingers() {
        return singerMapper.convertToDtoList(singerRepository.findTop10ByOrderByCreatedAtDesc());
    }

    public List<SongDto> getSongFromSinger(Long id) {

        return songMapper.convertToDtoList(singerRepository.findSongById(id));

    }


}
