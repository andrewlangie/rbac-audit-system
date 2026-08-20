package com.rbacaudit.dto;

import java.util.Set;

public record AuthResponse (
        String token,
        String username,
        Set<String> roles
) {
}