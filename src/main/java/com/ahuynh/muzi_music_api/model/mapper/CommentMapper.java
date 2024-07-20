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
            BeanUtils.copyProperties(dto, entity, "users", "time");
            entity.setUser(userMapper.convertToEntity(dto.getUser()));
        }
        return entity;
    }

    @Override
    public CommentDto convertToDto(Comment entity, Object... args) {
        CommentDto dto = new CommentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "parentCommentId", "users", "createdAt", "updatedAt");
            dto.setUser(userMapper.convertToDto(entity.getUser()));
            if (entity.getParentComment() != null)
                dto.setParentCommentId(entity.getParentComment().getId());
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
        }
        return dto;
    }


}