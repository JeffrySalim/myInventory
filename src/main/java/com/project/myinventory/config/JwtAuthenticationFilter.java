package com.project.myinventory.config;

import com.project.myinventory.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Get Header Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Cek Format token Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.info("Request tanpa token terdeteksi untuk URL: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // Ambil token dari bearer
        try {
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);

            // cek username ada tapi user belum di autentikasi
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.isTokenValid(jwt, username)){
                    String role = jwtService.extractRole(jwt);
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                    // Buat objek autentikasi
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            username,null,List.of(authority)
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e){
            log.error("Usernamemu tidak bisa: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
