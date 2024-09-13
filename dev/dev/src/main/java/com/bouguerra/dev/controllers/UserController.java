package com.bouguerra.dev.controllers;

import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bouguerra.dev.config.JwtUtil;
import com.bouguerra.dev.dto.UserDTO;
import com.bouguerra.dev.exceptions.AvatarProcessingException;
import com.bouguerra.dev.exceptions.EmailAlreadyExistsException;
import com.bouguerra.dev.exceptions.UserNotFoundException;
import com.bouguerra.dev.exceptions.UsernameAlreadyExistsException;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.UserRepository;
import com.bouguerra.dev.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
@Autowired
JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/info")
    public ResponseEntity<UserDTO> getUserInfo() {
        try {
            UserDTO user = userService.getCurrentUser();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/current-profile-picture/{userId}")
    public ResponseEntity<String> getCurrentProfilePictureBase64() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String usernameFromAuth = ((UserDetails) authentication.getPrincipal()).getUsername();
            User user = userRepository.findByUsername(usernameFromAuth)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
            String base64Image = userService.getProfilePictureBase64(user.getId());
            return ResponseEntity.ok(base64Image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/profile-picture/{userId}")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long userId) {
        Optional<byte[]> optionalImage = userService.getProfilePicture(userId);

        if (optionalImage.isPresent()) {
            byte[] image = optionalImage.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); 
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
   
    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestPart(required = false) MultipartFile picture
    ) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String usernameFromAuth = ((UserDetails) authentication.getPrincipal()).getUsername();
            User user = userRepository.findByUsername(usernameFromAuth)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    
            Long userId = user.getId();
            
            UserDTO updatedUser = userService.updateUser(userId, username, email, picture);
    
            String newToken = jwtUtil.refreshToken(authentication.getCredentials().toString());
    
            return ResponseEntity.ok(Map.of("user", updatedUser, "token", newToken));
        } catch (UserNotFoundException | UsernameAlreadyExistsException | EmailAlreadyExistsException | AvatarProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
}
