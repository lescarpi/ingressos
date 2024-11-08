package com.ingressos.api.http.request;

public record UserLoginRequest(
        String email,
        String password
) {
}
