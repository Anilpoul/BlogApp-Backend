package com.blogapp.repositories;

import com.blogapp.models.User;
import com.blogapp.models.Role; 
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByRoles(Role role);
}
