package com.ahuynh.muzi_music_api.model.mapper;

import com.ahuynh.muzi_music_api.model.dto.NotificationDto;
import com.ahuynh.muzi_music_api.model.entity.notification.Notification;
import com.ahuynh.muzi_music_api.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationMapper extends BaseMapper<Notification, NotificationDto> {

    private final UserMapper userMapper;


    @Override
    public Notification convertToEntity(NotificationDto dto, Object... args) {
        Notification entity = new Notification();
        if (dto != null) {
            BeanUtils.copyProperties(dto, entity);

        }
        return entity;
    }

    @Override
    public NotificationDto convertToDto(Notification entity, Object... args) {
        NotificationDto dto = new NotificationDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto, "createdAt", "updatedAt", "time", "user","songId","commentId");
            dto.setUser(userMapper.convertToDto(entity.getUser()));
            dto.setTime(Utils.getTime(entity.getCreatedAt()));
            dto.setSongId(entity.getSong().getId());
            dto.setSongId(entity.getComment().getId());
            dto.setCreatedAt(Utils.convertInstantToTime(entity.getCreatedAt()));
            dto.setUpdatedAt(Utils.convertInstantToTime(entity.getUpdatedAt()));
        }
        return dto;
    }
}