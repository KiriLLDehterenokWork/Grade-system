package com.example.gradesystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping({"/", "/default"})
    public String defaultAfterLogin(Authentication authentication) {
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            if ("ROLE_ADMIN".equals(role)) {
                return "redirect:/admin";
            } else if ("ROLE_TEACHER".equals(role)) {
                return "redirect:/teacher";
            } else if ("ROLE_STUDENT".equals(role)) {
                return "redirect:/student";
            }
        }
        return "redirect:/login?error";
    }
}