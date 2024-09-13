package com.bouguerra.dev.services;

 import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bouguerra.dev.dto.UserDTO;
import com.bouguerra.dev.models.User;
 
public interface UserService {

        UserDTO getCurrentUser();

     Optional<byte[]> getProfilePicture(Long userId);
     byte[] getUserImage(Long userId);
     UserDTO updateUser(Long userId, String newUsername, String newEmail, MultipartFile newPicture) ;
     String getProfilePictureBase64(Long userId);
     User findByUsername(String username);
}
