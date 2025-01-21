package com.phegondev.InventoryManagementSystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "suppliers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier {

    @Id
    private String id;  // SupplierId

    @NotBlank(message = "Name is required")
    private String name;

    private String address;

    private String contactInfo;  // ContactInfo field added

    private String productsProvided;  // ProductsProvided field added
}
