package com.phegondev.InventoryManagementSystem.service.impl;

import com.phegondev.InventoryManagementSystem.dto.LoginRequest;
import com.phegondev.InventoryManagementSystem.dto.RegisterRequest;
import com.phegondev.InventoryManagementSystem.dto.Response;
import com.phegondev.InventoryManagementSystem.dto.UserDTO;
import com.phegondev.InventoryManagementSystem.entity.User;
import com.phegondev.InventoryManagementSystem.enums.UserRole;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import com.phegondev.InventoryManagementSystem.exceptions.NotFoundException;
import com.phegondev.InventoryManagementSystem.repository.UserRepository;
import com.phegondev.InventoryManagementSystem.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response createUser(UserDTO userDTO) {
        // Map UserDTO to User entity
        User userToSave = modelMapper.map(userDTO, User.class);
        userRepository.save(userToSave);
        return Response.builder()
                .status(200)
                .message("User created successfully")
                .build();
    }

    @Override
    public Response registerUser(RegisterRequest registerRequest) {
//        UserRole role = UserRoleEnum.MANAGER;
//        if (registerRequest.getRole() != null) {
//            role = registerRequest.getRole();
//        }
//        User userToSave = User.builder()
//                .name(registerRequest.getName())
//                .email(registerRequest.getEmail())
//                .password(registerRequest.getPassword())
//                .phoneNumber(registerRequest.getPhoneNumber())
//                .role(role)
//                .build();
//        userRepository.save(userToSave);
        return Response.builder()
                .status(200)
                .message("User created successfully")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("Email not found"));

        // Password verification and JWT handling can be reintroduced if required
        return Response.builder()
                .status(200)
                .message("User logged in successfully")
//                .role(user.getRole())
                .expirationTime(6)
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<UserDTO> userDTOS = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
        userDTOS.forEach(userDTO -> userDTO.setTransactions(null));
        return Response.builder()
                .status(200)
                .message("Success")
                .users(userDTOS)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        throw new UnsupportedOperationException("Method not implemented for MongoDB yet");
    }



    @Override
    public Response updateUser(String id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userDTO.getEmail() != null) existingUser.setEmail(userDTO.getEmail());
        if (userDTO.getName() != null) existingUser.setName(userDTO.getName());
        if (userDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(userDTO.getPhoneNumber());
        if (userDTO.getRole() != null) existingUser.setRole(userDTO.getRole());

        userRepository.save(existingUser);
        return Response.builder()
                .status(200)
                .message("User successfully updated")
                .build();
    }

    @Override
    public Response deleteUser(String id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.deleteById(id);
        return Response.builder()
                .status(200)
                .message("User successfully deleted")
                .build();
    }

    @Override
    public Response getUserTransactions(String id) {
        System.out.println("id"+ id );
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        System.out.println(userDTO);
        if (userDTO.getTransactions()==null){
            return Response.builder()
                    .status(200)
                    .message("ZeroTransactionFound")
                    .user(userDTO)
                    .build();
        }
//        userDTO.getTransactions().forEach(transactionDTO -> {
//            transactionDTO.setProductId(null);
//            transactionDTO.setSupplierId(null);
//        }
//        );
        return Response.builder()
                .status(200)
                .message("Success")
                .user(userDTO)
                .build();
    }
}
