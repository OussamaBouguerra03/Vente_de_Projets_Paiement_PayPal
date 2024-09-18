package com.bouguerra.dev.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import com.bouguerra.dev.dto.UserDTO;
import com.bouguerra.dev.exceptions.AvatarProcessingException;
import com.bouguerra.dev.exceptions.EmailAlreadyExistsException;
import com.bouguerra.dev.exceptions.UserNotFoundException;
import com.bouguerra.dev.exceptions.UsernameAlreadyExistsException;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository; // Votre repository

    @Override
    public Optional<byte[]> getProfilePicture(Long userId) {
        return userRepository.findById(userId)
            .map(user -> user.getPicture());
    }
@Override
    public String getProfilePictureBase64(Long userId) {
        Optional<byte[]> pictureData = getProfilePicture(userId);
        return pictureData.map(picture -> Base64.getEncoder().encodeToString(picture))
                          .orElseThrow(() -> new RuntimeException("No picture found for user"));
    }
    public User findByUsername(String username) {
    return userRepository.findByUsername(username)
                         .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
}

    @Override
 public UserDTO getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
        throw new RuntimeException("No authentication found");
    }
    String username = ((UserDetails) authentication.getPrincipal()).getUsername();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPicture());
}

    
    
    @Override
    public byte[] getUserImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPicture(); // Supposons que `User` a un attribut `image` pour stocker l'image
    }
 
       @Override
       public UserDTO updateUser(Long userId, String newUsername, String newEmail, MultipartFile newPicture) {
           User user = userRepository.findById(userId)
               .orElseThrow(() -> new UserNotFoundException("User not found"));
       
           // Vérification si le nouveau nom d'utilisateur ou email est déjà utilisé
           if (userRepository.existsByUsername(newUsername) && !user.getUsername().equals(newUsername)) {
               throw new UsernameAlreadyExistsException("Username is already in use");
           }
           if (userRepository.existsByEmail(newEmail) && !user.getEmail().equals(newEmail)) {
               throw new EmailAlreadyExistsException("Email is already in use");
           }
       
           // Mise à jour des informations de l'utilisateur
           user.setUsername(newUsername);
           user.setEmail(newEmail);
       
           // Mise à jour de la photo de profil si elle est présente
           if (newPicture != null && !newPicture.isEmpty()) {
               try (InputStream inputStream = newPicture.getInputStream()) {
                   user.setPicture(convertInputStreamToByteArray(inputStream));
               } catch (IOException e) {
                   throw new AvatarProcessingException("Failed to process avatar file", e);
               }
           }
       
           User updatedUser = userRepository.save(user);
           return new UserDTO(updatedUser.getId(), updatedUser.getUsername(), updatedUser.getEmail(), updatedUser.getPicture());
       }
       

    private byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        }
    }

}
