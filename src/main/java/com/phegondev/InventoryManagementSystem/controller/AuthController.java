package com.phegondev.InventoryManagementSystem.controller;

import com.phegondev.InventoryManagementSystem.dto.LoginRequest;
import com.phegondev.InventoryManagementSystem.dto.RegisterRequest;
import com.phegondev.InventoryManagementSystem.dto.Response;
import com.phegondev.InventoryManagementSystem.entity.User;
import com.phegondev.InventoryManagementSystem.enums.UserRole;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import com.phegondev.InventoryManagementSystem.repository.RoleRepository;
import com.phegondev.InventoryManagementSystem.service.UserService;
import com.phegondev.InventoryManagementSystem.service.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/autg")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid RegisterRequest registerRequest){
        System.out.println(registerRequest.getPassword());
        return ResponseEntity.ok(authenticationService.signup(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody @Valid LoginRequest loginRequest){
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }
}
