package com.ingressos.api.http;

import com.ingressos.api.model.Role;

import java.util.List;

public record UserResponse(
        Long id,
        String email,
        List<Role> roles
) {
}
