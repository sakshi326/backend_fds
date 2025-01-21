package com.phegondev.InventoryManagementSystem.repository;

import com.phegondev.InventoryManagementSystem.enums.UserRole;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<UserRole, String> {
    Optional<UserRole> findByroleName(UserRoleEnum roleName);
}
