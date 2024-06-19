package com.ahuynh.muzi_music_api.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value(value = "${com.ahuynh.jwtSecret}")
    private String jwtSecret;

    @Value(value = "${com.ahuynh.jwtExp}")
    private Long jwtExpiration;

    //Tạo token
    public String generateToken(Authentication authentication) {
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts
                .builder()
                .subject(Long.toString(userDetail.getId()))
                .issuedAt(new Date())
                .expiration(expiryDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    //Decode private key jwt
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //Lấy userid từ claim
    public Long getUserIdFromJWT(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    //Lấy claim từ token
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //Xác thực token
    public boolean validateToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims != null;

        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token format: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT type: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT arguments: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during JWT validation: {}", e.getMessage());
        }
        return false;
    }
}
