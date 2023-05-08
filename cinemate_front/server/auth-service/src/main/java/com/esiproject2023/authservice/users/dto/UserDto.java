package com.esiproject2023.authservice.users.dto;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Id
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private String password;
}
