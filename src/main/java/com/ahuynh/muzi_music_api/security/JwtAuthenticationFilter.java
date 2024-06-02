package com.ahuynh.muzi_music_api.security;

import com.ahuynh.muzi_music_api.service.CustomUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Lọc ra token từ request nếu hợp lệ cấp quyền cho user
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailService customUserDetailService;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull  FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt) ) {
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);
                log.info(userId.toString());
                CustomUserDetail userDetail = customUserDetailService.loadUserById(userId);
                log.info(userDetail.toString());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetail, null, userDetail.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                log.info("Authenticated user 1: {}{}", userDetail.getUsername(),userDetail.getAuthorities());
                log.info("Authenticated user 2: {}{}", userDetail.getPassword(),userDetail.getEmail());
                log.info("Authenticated user 3: {}", userDetail.getId());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                log.info("Authenticated user 4: {}", userDetail.getId());
            }
        } catch (Exception e) {
            log.error("Cannot set user auth {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
        log.info("Authenticated user 5: {}", "ABC");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
