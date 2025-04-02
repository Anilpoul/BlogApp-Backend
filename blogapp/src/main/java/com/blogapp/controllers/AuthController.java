package com.blogapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/auth")
    public String showAuthPage() {
        return "auth"; // This must match `auth.html` in `templates/`
    }
}
