package com.pdmadmin.pdmadmin.util;

import com.pdmadmin.pdmadmin.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    @Resource
    private JwtProperties props;
    private SecretKey key;

    @PostConstruct
    public void init() {
        if (props.getSecret() == null) {
            key = Keys.hmacShaKeyFor("pdmadminpdmadminpdmadminpdmadminpdmadminpdmadmin".getBytes());
        } else {
            key = Keys.hmacShaKeyFor(props.getSecret().getBytes());
        }
    }

    public String createToken(Long userId, String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + props.getExpireSeconds() * 1000);
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .addClaims(Map.of("uid", userId, "uname", username))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }
}
