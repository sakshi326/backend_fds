package com.phegondev.InventoryManagementSystem.dto;

import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Id
    private String id;  // MongoDB requires an ID field, typically of type String

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "PhoneNumber is required")
    private String phoneNumber;

    private UserRoleEnum role;
}
