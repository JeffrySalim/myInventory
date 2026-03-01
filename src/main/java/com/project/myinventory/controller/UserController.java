package com.project.myinventory.controller;

import com.project.myinventory.dto.LoginRequestDTO;
import com.project.myinventory.dto.UserResponseDTO;
import com.project.myinventory.entity.Users;
import com.project.myinventory.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@Valid @RequestBody Users user){

        userService.registerUser(user);
        Map<String, String> response = new HashMap<>();
        response.put("Message: ","Register Berhasil");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequestDTO loginRequest){
        Map<String, Object> login = userService.login(loginRequest);
        String token = (String) login.get("token");
        UserResponseDTO user = (UserResponseDTO) login.get("user");

        Map<String, Object> response = new HashMap<>();
        response.put("Message: ","Login Berhasil");
        response.put("User",user);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(response);
    }

}
