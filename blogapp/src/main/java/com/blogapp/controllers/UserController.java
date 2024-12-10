package com.blogapp.controllers;

import com.blogapp.payloads.ApiResponse;
import com.blogapp.payloads.UserDto;
import com.blogapp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {

    @Autowired
    private UserService userService;
    // Post - create user
    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto createdUser = this.userService.createUser(userDto);
        return  new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // PUT - update user
    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> upadateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer uid){
        UserDto updatedUser = this.userService.updateUser(userDto, uid);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE - delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid){
        this.userService.deleteUser(uid);
        return new  ResponseEntity(new ApiResponse("user Deleted Successfully", true), HttpStatus.OK);
    }

    // GET - get user
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(){

        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getSingleUser(@PathVariable Integer userId){
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }

}
