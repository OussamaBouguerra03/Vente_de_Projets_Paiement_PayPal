package com.bouguerra.dev.controllers;

import com.bouguerra.dev.dto.UserRegisterDTO;
import com.bouguerra.dev.exceptions.EmailAlreadyExistsException;
import com.bouguerra.dev.exceptions.EmailSendingException;
import com.bouguerra.dev.exceptions.InvalidTokenException;
import com.bouguerra.dev.exceptions.UserNotFoundException;
import com.bouguerra.dev.exceptions.UsernameAlreadyExistsException;
import com.bouguerra.dev.models.Role;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.config.JwtUtil;
import com.bouguerra.dev.dto.PasswordResetDTO;
import com.bouguerra.dev.dto.PasswordResetRequestDTO;
import com.bouguerra.dev.dto.UserLoginDTO;
import com.bouguerra.dev.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
  
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("username") String username,
                                       @RequestParam("email") String email,
                                       @RequestParam("password") String password,
                                       @RequestParam("role") Role role,
                                       @RequestParam(value = "picture", required = false) MultipartFile avatarFile) {
        try {
            UserRegisterDTO registerDTO = new UserRegisterDTO(username, email, password, avatarFile, role);
            User user = authService.registerUser(registerDTO, avatarFile);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Username already exists"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        Map<String, Object> response = authService.loginUser(userLoginDTO);
        return ResponseEntity.ok(response);
    }
  
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token, 
                                                 @Valid @RequestBody PasswordResetDTO resetDTO, 
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(". ");
            }
            return ResponseEntity.badRequest().body("{\"message\": \"" + errorMessage.toString() + "\"}");
        }
    
        try {
            authService.resetPassword(token, resetDTO);
            return ResponseEntity.ok("Password reset successfully.");
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"Invalid or expired token.\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }
    
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody PasswordResetRequestDTO requestDTO) {
        try {
            authService.sendPasswordResetEmail(requestDTO);
            return ResponseEntity.ok("Password reset email sent.");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"User not found.\"}");
        } catch (EmailSendingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error sending email.\"}");
        }
    }

   
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String oldToken) {
        try {
            String newToken = jwtUtil.refreshToken(oldToken);
            return ResponseEntity.ok(newToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
