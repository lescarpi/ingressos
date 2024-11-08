package com.ingressos.api.controller;

import com.ingressos.api.http.CreateUserRequest;
import com.ingressos.api.http.JwtTokenResponse;
import com.ingressos.api.http.UserLoginRequest;
import com.ingressos.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> authenticateUser(@RequestBody UserLoginRequest request) {
        return service.authenticateUser(request);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        return service.createUser(request);
    }

}
