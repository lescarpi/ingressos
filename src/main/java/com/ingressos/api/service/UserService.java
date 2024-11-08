package com.ingressos.api.service;

import com.ingressos.api.config.SecurityConfiguration;
import com.ingressos.api.config.UserDetailsImpl;
import com.ingressos.api.http.request.CreateUserRequest;
import com.ingressos.api.http.response.JwtTokenResponse;
import com.ingressos.api.http.request.UserLoginRequest;
import com.ingressos.api.model.Role;
import com.ingressos.api.model.User;
import com.ingressos.api.repository.RoleRepository;
import com.ingressos.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

    public ResponseEntity<JwtTokenResponse> authenticateUser(UserLoginRequest request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return new ResponseEntity<>(new JwtTokenResponse(jwtTokenService.generateToken(userDetails)), HttpStatus.OK);
    }

    public ResponseEntity<Void> createUser(CreateUserRequest request) {
        Role role = roleRepository.findByName(request.role())
                .orElseGet(() -> roleRepository.save(Role.builder().name(request.role()).build()));

        String encodedPassword = passwordEncoder.encode(request.password());
        User user = User.builder()
                .email(request.email())
                .password(encodedPassword)
                .roles(List.of(role))
                .build();
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
