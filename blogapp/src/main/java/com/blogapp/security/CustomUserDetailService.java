package com.blogapp.security;

import com.blogapp.exceptions.ResourceNotFoundException;
import com.blogapp.models.User;
import com.blogapp.models.UserDetailsImpl;
import com.blogapp.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // loading user from database by userName
        User user = this.userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException(" User ", " email "+username, 0));

        return new UserDetailsImpl(user);
    }
}
