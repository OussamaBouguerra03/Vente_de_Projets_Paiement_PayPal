package com.bouguerra.dev.dto;
import org.springframework.web.multipart.MultipartFile;

import com.bouguerra.dev.models.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {
    private String username;
    private String email;
    private String password;
      private MultipartFile picture;
    private Role role;
}
