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

    List<UserDto> getUsersByRole(String role); 

    UserDetails loadUserByUsername(String username); 
}
