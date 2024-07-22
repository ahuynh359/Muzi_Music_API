package com.ahuynh.muzi_music_api.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "verification_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VerificationToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "expiry_time", nullable = false, updatable = false)
    private Instant expiryTime = Instant.now().plusSeconds(60);

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public VerificationToken(User user, String token) {
        this.user = user;
        this.token = token;
    }



}