package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.utils.Utils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class CommentMapper extends BaseMapper<Comment, CommentDto> {
    private UserMapper userMapper = new UserMapper();

    @Override
    public Comment convertToEntity(CommentDto dto, Object... args) {
        Comment entity = new Comment();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "users", "replies");
            entity.setUser(userMapper.convertToEntity(dto.getUser()));
            entity.setReplies(this.convertToEntitySet(dto.getReplies()));
        }
        return entity;
    }

    @Override
    public CommentDto convertToDto(Comment entity, Object... args) {
        CommentDto dto = new CommentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "replies", "users", "createdAt", "updatedAt", "time");
            dto.setUser(userMapper.convertToDto(entity.getUser()));
            dto.setReplies(this.convertToDtoSet(entity.getReplies()));
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
            dto.setTime(Utils.getTime(entity.getCreatedAt()));
        }
        return dto;
    }


}