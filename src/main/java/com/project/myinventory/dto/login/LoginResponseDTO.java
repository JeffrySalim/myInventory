package com.project.myinventory.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    private String username;
    private String fullname;
    private String email;
    private String role;
}
