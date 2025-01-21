package com.phegondev.InventoryManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "responses") // Specify MongoDB collection
public class Response {

    @Id
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
