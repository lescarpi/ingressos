package com.ingressos.api.http;

public record UserLoginRequest(
        String email,
        String password
) {
}
