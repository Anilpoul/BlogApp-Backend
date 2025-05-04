package com.blogapp.services.impl;

import com.blogapp.exceptions.EmailAlreadyExistsException;
import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.models.User;
import com.blogapp.models.Role;
import com.blogapp.payloads.UserDto;
import com.blogapp.repositories.UserRepo;
import com.blogapp.security.CustomUserDetailService;
import com.blogapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepo.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User with email '" + userDto.getEmail() + "' already exists.");
        }

        userDto.setPassword(encoder.encode(userDto.getPassword()));

        if (userDto.getEmail().endsWith("@admin.com")) {
            userDto.setRoles(Set.of(Role.ROLE_ADMIN));
        } else {
            userDto.setRoles(Set.of(Role.ROLE_USER));
        }

        User user = this.dtoToUser(userDto);
        User savedUser = this.userRepo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(userDto.getPassword()));
        }

        user.setAbout(userDto.getAbout());

        if (userDto.getRoles() != null) {
            user.setRoles(userDto.getRoles());
        }

        User updatedUser = this.userRepo.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        return users.stream().map(this::userToDto).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        this.userRepo.delete(user);
    }

    @Override
    public List<UserDto> getUsersByRole(String role) {
        List<User> users = userRepo.findByRoles(Role.valueOf(role.toUpperCase()));
        return users.stream().map(this::userToDto).collect(Collectors.toList());
    }

    public User dtoToUser(UserDto userDto) {
        return this.modelMapper.map(userDto, User.class);
    }

    public UserDto userToDto(User user) {
        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return customUserDetailService.loadUserByUsername(username); 
    }
}
