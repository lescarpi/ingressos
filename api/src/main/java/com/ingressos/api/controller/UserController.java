package com.ingressos.api.controller;

import com.ingressos.api.controller.interfaces.UserControllerInterface;
import com.ingressos.api.http.request.CreateUserRequest;
import com.ingressos.api.http.request.UserLoginRequest;
import com.ingressos.api.http.response.JwtTokenResponse;
import com.ingressos.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserControllerInterface {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<JwtTokenResponse> authenticateUser(UserLoginRequest request) {
        return service.authenticateUser(request);
    }

    @Override
    public ResponseEntity<Void> createUser(CreateUserRequest request) {
        return service.createUser(request);
    }

}
