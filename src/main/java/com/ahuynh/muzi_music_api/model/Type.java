package com.ahuynh.muzi_music_api.model;

import com.ahuynh.muzi_music_api.model.role.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type")
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"created_at"},
        allowGetters = true
)
@NoArgsConstructor
@AllArgsConstructor
public class Type  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, unique = true)
    private String name;

    @Size(max = 50)
    private String description;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "song_type"
            , joinColumns = @JoinColumn(name = "type_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "song_id", referencedColumnName = "id"))
    private List<Song> songs = new ArrayList<>();



    public Type(String name , String description){
        this.name = name;
        this.description = description;

    }


}