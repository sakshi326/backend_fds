package com.phegondev.InventoryManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

//    @Id
//    private String id;  // MongoDB requires an ID field, typically of type String
    @Id
//    @Positive(message = "Product id is required")
    private String productId;

    @Positive(message = "Quantity id is required")
    private String quantity;

    private String supplierId;

    private String description;
}
