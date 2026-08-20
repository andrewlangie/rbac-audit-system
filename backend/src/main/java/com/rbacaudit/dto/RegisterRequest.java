package com.rbacaudit.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Username required")
    @Size(min = 3, max = 50, message = "username is between 3 and 15 12 characters")
    private String username;

    @NotBlank(message = "Email required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    @Pattern (
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>]).*$",
            message = "Password must contain at least one special character"
    )
    private String password;
}
