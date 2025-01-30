package com.blogapp.repositories;

import com.blogapp.models.User;
import com.blogapp.models.Role;  // Import Role model
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    // ✅ Find users by role
    List<User> findByRoles(Role role); // This will return users who have the given role
}
