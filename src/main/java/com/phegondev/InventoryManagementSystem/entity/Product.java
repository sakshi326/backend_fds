package com.phegondev.InventoryManagementSystem.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Sku is required")
    private String sku;

    @Positive(message = "Product price must be a positive value")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity cannot be lesser than zero")
    private Integer stockQuantity;

    private String description;
    private String imageUrl;
    private LocalDateTime expiryDate;
    private LocalDateTime updatedAt;
//    private final LocalDateTime createdAt = LocalDateTime.now();

    @DBRef
    private Category category;

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", expiryDate=" + expiryDate +
                ", updatedAt=" + updatedAt +
//                ", createdAt=" + createdAt +
                '}';
    }
}
