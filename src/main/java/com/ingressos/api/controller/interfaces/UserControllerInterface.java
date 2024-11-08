package com.ingressos.api.controller.interfaces;

import com.ingressos.api.http.request.CreateUserRequest;
import com.ingressos.api.http.request.UserLoginRequest;
import com.ingressos.api.http.response.JwtTokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
public interface UserControllerInterface {

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> authenticateUser(@RequestBody UserLoginRequest request);

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request);

}
