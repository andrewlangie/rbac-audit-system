package com.rbacaudit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginRequest{
    @NotBlank(message =  "username is required")
    private String username;

    @NotBlank(message =  "Password is required")
    private String password;
}
