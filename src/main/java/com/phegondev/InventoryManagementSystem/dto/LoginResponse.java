package com.phegondev.InventoryManagementSystem.dto;

import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class LoginResponse {
    private String id;

    // Generic
    private int status;
    private String message;

    // For login
    private String token;
    private UserRoleEnum role;
    private long expirationTime;

    // For pagination
    private Integer totalPages;
    private Long totalElements;

    // Data output optional
    private UserDTO user;
    private List<UserDTO> users;
    private SupplierDTO supplier;
    private List<SupplierDTO> suppliers;
    private CategoryDTO category;
    private List<CategoryDTO> categories;
    private ProductDTO product;
    private List<ProductDTO> products;
    private TransactionDTO transaction;
    private List<TransactionDTO> transactions;

    private final LocalDateTime timestamp = LocalDateTime.now();
}
