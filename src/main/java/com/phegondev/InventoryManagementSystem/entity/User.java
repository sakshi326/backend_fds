package com.phegondev.InventoryManagementSystem.entity;

import com.phegondev.InventoryManagementSystem.enums.UserRole;
import com.phegondev.InventoryManagementSystem.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String id; // MongoDB uses String type IDs by default

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Field("email") // Optional: explicitly map to "email" field
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Phone Number is required")
    @Field("phone_number") // Explicit mapping for field names with underscores
    private String phoneNumber;

    private UserRole role;

    private List<String> transactionIds; // Store references to transactions by their IDs

    @CreatedDate
    @Field("created_at") // Explicit mapping for consistent naming
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }


   public String getPassword() {
        return password;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRoleName());

        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

//    @Override
//    public boolean isAccountNonExpired() {
////          new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toString());
//
//        return UserDetails.super.isAccountNonExpired();
//    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
////        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toString());
//
//        return List.of();
//    }

//
//    @Override
//    public String getEmail() {
//        return email;
//    }
//
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//}
}
