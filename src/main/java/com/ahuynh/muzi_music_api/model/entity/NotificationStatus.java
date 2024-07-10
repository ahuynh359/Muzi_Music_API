package com.ahuynh.muzi_music_api.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

public enum NotificationStatus {
    NOT_READ,
    READ,

}
