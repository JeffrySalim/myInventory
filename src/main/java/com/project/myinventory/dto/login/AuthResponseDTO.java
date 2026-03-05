package com.project.myinventory.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

    private String token;
    private LoginResponseDTO user;
}
