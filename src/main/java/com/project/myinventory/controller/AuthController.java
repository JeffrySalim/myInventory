package com.project.myinventory.controller;

import com.project.myinventory.dto.login.LoginRequestDTO;
import com.project.myinventory.dto.login.RegisterRequestDTO;
import com.project.myinventory.dto.login.AuthResponseDTO;
import com.project.myinventory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        AuthResponseDTO response = authService.login(requestDTO);
        return ResponseEntity.ok(response);
    }
}
