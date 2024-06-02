package com.ahuynh.muzi_music_api.security;

import com.ahuynh.muzi_music_api.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Slf4j
public class CustomUserDetail implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;


    public static CustomUserDetail createUserDetail(User user) {
        // Ứng với từng role của người dùng -> Lọc ra ds name role
        List<GrantedAuthority> authorities = user
                .getRole()
                .stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new CustomUserDetail(user.getId(), user.getEmail(), user.getPassword(), user.getUsername(), authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        log.info(authorities.toString());
        return authorities == null ? null : new ArrayList<>(authorities);

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
