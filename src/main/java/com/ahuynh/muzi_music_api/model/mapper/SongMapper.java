package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.AlbumDto;
import com.ahuynh.muzi_music_api.model.dto.SingerDto;
import com.ahuynh.muzi_music_api.model.dto.SongDto;
import com.ahuynh.muzi_music_api.model.dto.TypeDto;
import com.ahuynh.muzi_music_api.model.entity.Song;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SongMapper extends BaseMapper<Song, SongDto> {
    private final SingerMapper singerMapper;
    private final TypeMapper typeMapper;
    private final AlbumMapper albumMapper ;

    public SongMapper(SingerMapper singerMapper, TypeMapper typeMapper, AlbumMapper albumMapper) {
        super();
        this.singerMapper = singerMapper;
        this.typeMapper = typeMapper;
        this.albumMapper = albumMapper;
    }


    @Override
    public Song convertToEntity(SongDto dto, Object... args) {
        Song entity = new Song();
        if (dto != null) {

            BeanUtils.copyProperties(dto, entity,"album","singers","types");
            entity.setAlbum(albumMapper.convertToEntity(dto.getAlbum()));
            entity.setSingers(singerMapper.convertToEntitySet(dto.getSingers()));
            entity.setTypes(typeMapper.convertToEntitySet(dto.getTypes()));

        }
        return entity;
    }

    @Override
    public SongDto convertToDto(Song entity, Object... args) {
        SongDto dto = new SongDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto,"album","singers","types");
            dto.setAlbum(albumMapper.convertToDto(entity.getAlbum()));
            dto.setSingers(singerMapper.convertToDtoSet(entity.getSingers()));
            dto.setTypes(typeMapper.convertToDtoSet(entity.getTypes()));
        }
        return dto;
    }
}