package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.CommentDto;
import com.ahuynh.muzi_music_api.model.entity.Comment;
import com.ahuynh.muzi_music_api.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper extends BaseMapper<Comment, CommentDto> {
    private  final UserMapper userMapper ;

    @Override
    public Comment convertToEntity(CommentDto dto, Object... args) {
        Comment entity = new Comment();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity, "users");
            entity.setUser(userMapper.convertToEntity(dto.getUser()));
        }
        return entity;
    }

    @Override
    public CommentDto convertToDto(Comment entity, Object... args) {
        CommentDto dto = new CommentDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "users", "createdAt", "updatedAt", "time", "commentParentId");
            dto.setUser(userMapper.convertToDto(entity.getUser()));
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
            dto.setTime(Utils.getTime(entity.getCreatedAt()));
            if (entity.getCommentParent() != null)
                dto.setCommentParentId(entity.getCommentParent().getId());
        }
        return dto;
    }


}