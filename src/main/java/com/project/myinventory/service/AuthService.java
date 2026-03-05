package com.project.myinventory.service;

import com.project.myinventory.dto.login.LoginRequestDTO;
import com.project.myinventory.dto.login.LoginResponseDTO;
import com.project.myinventory.dto.login.RegisterRequestDTO;
import com.project.myinventory.dto.login.AuthResponseDTO;
import com.project.myinventory.entity.User;
import com.project.myinventory.entity.enums.Role;
import com.project.myinventory.exception.ResourceAlreadyExistsException;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.exception.UnauthorizedException;
import com.project.myinventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void registerUser(RegisterRequestDTO requestDTO){

        if (userRepository.existsByUsername(requestDTO.getUsername())){
            throw new ResourceAlreadyExistsException("Username sudah ada, coba username baru");
        }
        if (userRepository.existsByEmail(requestDTO.getEmail())){
            throw new ResourceAlreadyExistsException("Email sudah ada, Coba email baru");
        }

        User user = User.builder()
                .username(requestDTO.getUsername())
                .fullname(requestDTO.getFullname())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public AuthResponseDTO login(LoginRequestDTO loginRequest) {

        User user = userRepository.findByUsernameAndDeletedAtIsNull(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User tidak ditemukan"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password yang dimasukkan salah");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());

        LoginResponseDTO userData = LoginResponseDTO.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();

        return AuthResponseDTO.builder()
                .token(token)
                .user(userData)
                .build();
    }
}
