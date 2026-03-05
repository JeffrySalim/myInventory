package com.project.myinventory.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @NotBlank(message = "Username harus diisi")
    private String username;

    @NotBlank(message = "Fullname harus diisi")
    private String fullname;

    @Email(message = "Format email salah")
    @NotBlank(message = "Email harus diisi")
    private String email;

    @NotBlank(message = "Password harus diisi")
    private String password;
}
