package com.project.myinventory.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotBlank(message = "Username harus diisi")
    private String username;

    @NotBlank(message = "Password harus diisi")
    private String password;
}
