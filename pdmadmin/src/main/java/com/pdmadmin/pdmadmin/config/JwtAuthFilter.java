package com.pdmadmin.pdmadmin.config;

import com.pdmadmin.pdmadmin.util.JwtUtil;
import com.pdmadmin.pdmadmin.util.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Resource
    private JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/api/auth/login") || uri.contains("/api/auth/register")) return true;
        if (uri.contains("/swagger") || uri.contains("/v3/api-docs")) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String header = request.getHeader("Authorization");
            if (header == null || header.isBlank()) {
                header = request.getHeader("pdmtoken");
                if (header != null && !header.isBlank()) header = "Bearer " + header;
            }
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.substring(7);
                Jws<Claims> jws = jwtUtil.parse(token);
                Claims claims = jws.getBody();
                Object uid = claims.get("uid");
                if (uid != null) {
                    Long id = Long.valueOf(String.valueOf(uid));
                    UserContext.set(id);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}
