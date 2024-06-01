package com.ahuynh.muzi_music_api.email;

import com.ahuynh.muzi_music_api.model.User;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private User user;



    public OnRegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.appUrl = appUrl;
        this.user = user;

    }
}