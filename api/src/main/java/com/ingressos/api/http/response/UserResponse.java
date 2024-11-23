package com.ingressos.api.http.response;

import com.ingressos.api.model.Role;

import java.util.List;

public record UserResponse(
        Long id,
        String email,
        List<Role> roles
) {
}
