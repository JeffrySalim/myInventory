package com.project.myinventory.service;

import com.project.myinventory.dto.LoginRequestDTO;
import com.project.myinventory.dto.UserResponseDTO;
import com.project.myinventory.entity.Users;
import com.project.myinventory.exception.ResourceAlreadyExistsException;
import com.project.myinventory.exception.ResourceNotFoundException;
import com.project.myinventory.exception.UnauthorizedException;
import com.project.myinventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void registerUser(Users user){

        boolean cekUsername = userRepository.findByUsername(user.getUsername()).isPresent();
        boolean cekEmail = userRepository.findByEmail(user.getEmail()).isPresent();
        if (cekUsername){
            throw new ResourceAlreadyExistsException("Username: "+user.getUsername()+ " sudah ada, Coba username baru");
        }
        if (cekEmail){
            throw new ResourceAlreadyExistsException("Email: "+user.getEmail()+" sudah ada, Coba email baru");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole()==null){
            user.setRole("USER");
        }
        userRepository.save(user);
    }

    public Map<String, Object> login(LoginRequestDTO loginRequest) {

        Users user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Username: " + loginRequest.
                        getUsername() + " Tidak ditemukan"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password yang dimasukkan salah");
        }

        if (user.getDeletedAt() != null) {
            throw new UnauthorizedException("Akun anda sudah dihapus");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRole());

        UserResponseDTO userData = UserResponseDTO.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();


        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", userData);

        return result;
    }
}
