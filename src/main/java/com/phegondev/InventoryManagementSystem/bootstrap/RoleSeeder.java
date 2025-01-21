package com.phegondev.InventoryManagementSystem.bootstrap;

import com.phegondev.InventoryManagementSystem.enums.UserRole;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import com.phegondev.InventoryManagementSystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    RoleRepository roleRepository;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.loadRoles();
    }

    private void loadRoles() {
        UserRoleEnum[] roleNames = new UserRoleEnum[] { UserRoleEnum.USER, UserRoleEnum.MANAGER, UserRoleEnum.ADMIN };
        Map<UserRoleEnum, String> roleDescriptionMap = Map.of(
                UserRoleEnum.USER, "Default user role",
                UserRoleEnum.MANAGER, "Administrator role",
                UserRoleEnum.ADMIN, "Super Administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<UserRole> optionalRole = roleRepository.findByroleName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                UserRole roleToCreate = new UserRole();

                roleToCreate.setRoleName(roleName);
                roleToCreate.setRoleDescription(roleDescriptionMap.get(roleName));
                roleRepository.save(roleToCreate);
            });
        });
    }
}