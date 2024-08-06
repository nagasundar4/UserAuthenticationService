package com.example.userauthenticationservice.dtos;

import com.example.userauthenticationservice.models.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String email;

    private Role role;
}
