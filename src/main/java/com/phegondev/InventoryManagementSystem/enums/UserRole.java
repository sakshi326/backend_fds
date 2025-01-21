package com.phegondev.InventoryManagementSystem.enums;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "role")
@Data
public class UserRole {
    private UserRoleEnum roleName;
    private String roleDescription;
}
