package com.phegondev.InventoryManagementSystem.service.impl;

import com.phegondev.InventoryManagementSystem.dto.LoginRequest;
import com.phegondev.InventoryManagementSystem.dto.RegisterRequest;
import com.phegondev.InventoryManagementSystem.entity.User;
import com.phegondev.InventoryManagementSystem.enums.UserRole;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import com.phegondev.InventoryManagementSystem.repository.RoleRepository;
import com.phegondev.InventoryManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;



    public User signup(RegisterRequest input) {
        Optional<UserRole> optionalRoleUser = roleRepository.findByroleName(UserRoleEnum.USER);
        Optional<UserRole> optionalRoleManager = roleRepository.findByroleName(UserRoleEnum.MANAGER);
        System.out.println(input.getRole());
        User user = new User();
                user.setName(input.getName());
                user.setEmail(input.getEmail());
                user.setPassword(passwordEncoder.encode(input.getPassword()));
                if(input.getRole().equals("USER")){
                user.setRole(optionalRoleUser.get());}
                else{
                    user.setRole(optionalRoleManager.get());
                }

        return userRepository.save(user);
    }

    public User authenticate(LoginRequest input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }

    public UserRoleEnum getRole(String email){
        UserRole userRole=userRepository.findByEmail(email).get().getRole();
//        System.out.println(userRole);
    return userRole.getRoleName();
    }
}

