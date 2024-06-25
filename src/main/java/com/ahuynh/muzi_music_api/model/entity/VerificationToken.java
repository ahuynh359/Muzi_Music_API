package com.ahuynh.muzi_music_api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "verification_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class VerificationToken extends DateAudit {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Column(name = "expiry_time", nullable = false, updatable = false)
    private Instant expiryTime = Instant.now().plusSeconds(3600 * 10);

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @CreatedDate
    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    public VerificationToken(User user, String token) {
        this.user = user;
        this.token = token;
    }


}