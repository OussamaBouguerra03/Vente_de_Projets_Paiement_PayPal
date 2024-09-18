package com.bouguerra.dev.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        // Token invalidation is handled client-side
        return ResponseEntity.ok("Logged out successfully");
    }
}
