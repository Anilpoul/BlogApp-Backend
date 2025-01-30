package com.blogapp.services;

import com.blogapp.payloads.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user, Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);

    // 🔹 New Methods:
    List<UserDto> getUsersByRole(String role); // ✅ Fetch users based on role
    // 🔹 Add method to load user by username (email in your case)
    UserDetails loadUserByUsername(String username); // This will load UserDetails
}
