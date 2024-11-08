package com.ingressos.api.http;

import com.ingressos.api.model.RoleName;

public record CreateUserRequest(
        String email,
        String password,
        RoleName role
) {
}
