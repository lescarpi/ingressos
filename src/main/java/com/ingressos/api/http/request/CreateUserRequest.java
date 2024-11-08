package com.ingressos.api.http.request;

import com.ingressos.api.model.RoleName;

public record CreateUserRequest(
        String email,
        String password,
        RoleName role
) {
}
