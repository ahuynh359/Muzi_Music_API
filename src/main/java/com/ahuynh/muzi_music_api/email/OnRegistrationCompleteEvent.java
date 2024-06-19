package com.ahuynh.muzi_music_api.email;

import com.ahuynh.muzi_music_api.model.entity.User;
import com.ahuynh.muzi_music_api.model.entity.verification.VerificationType;
import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private User user;
    private VerificationType type;


    public OnRegistrationCompleteEvent(User user, String appUrl,VerificationType type) {
        super(user);
        this.appUrl = appUrl;
        this.user = user;
        this.type = type;

    }
}