package com.bouguerra.dev.services;

import com.bouguerra.dev.dto.UserRegisterDTO;
import com.bouguerra.dev.dto.PasswordResetDTO;
import com.bouguerra.dev.dto.PasswordResetRequestDTO;
import com.bouguerra.dev.dto.UserDTO;
import com.bouguerra.dev.dto.UserLoginDTO;
import com.bouguerra.dev.models.Mail;
import com.bouguerra.dev.models.User;

import java.util.Map;

 import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

public interface AuthService {
 User registerUser(UserRegisterDTO registerDTO, MultipartFile avatarFile);
    Map<String, Object> loginUser(UserLoginDTO userLoginDTO);
    User findByUsername(String username);
     void sendPasswordResetEmail(PasswordResetRequestDTO requestDTO);
    void resetPassword(String token, PasswordResetDTO resetDTO);
	public void sendEmail(Mail mail);
     String getUsernameById(Long id);
     Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
 }
