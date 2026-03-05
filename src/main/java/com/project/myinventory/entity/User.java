package com.project.myinventory.entity;

import com.project.myinventory.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Username harus diisi")
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank(message = "Fullname harus diisi")
    @Column(name = "fullname")
    private String fullname;

    @Email(message = "Format email salah")
    @NotBlank(message = "Email harus diisi")
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Password harus diisi")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role tidak boleh kosong")
    @Column(name = "role")
    private Role role;

}
