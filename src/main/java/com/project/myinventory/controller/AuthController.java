package com.project.myinventory.controller;

import com.project.myinventory.dto.login.LoginRequestDTO;
import com.project.myinventory.dto.login.LoginResponseDTO;
import com.project.myinventory.dto.login.RegisterRequestDTO;
import com.project.myinventory.dto.login.AuthResponseDTO;
import com.project.myinventory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        authService.registerUser(requestDTO);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registrasi berhasil");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        AuthResponseDTO response = authService.login(requestDTO);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", response.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(20*60*60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {

        ResponseCookie cleanCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .body(Map.of("message", "Berhasil logout"));
    }
}
