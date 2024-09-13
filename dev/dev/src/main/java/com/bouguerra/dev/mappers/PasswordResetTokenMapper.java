package com.bouguerra.dev.mappers;

 import com.bouguerra.dev.dto.PasswordResetTokenDTO;
import com.bouguerra.dev.models.PasswordResetToken;
import com.bouguerra.dev.models.User;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenMapper {

    // Convertir de l'entité à DTO
    public PasswordResetTokenDTO toDTO(PasswordResetToken token) {
        PasswordResetTokenDTO dto = new PasswordResetTokenDTO();
        dto.setToken(token.getToken());
        dto.setUserEmail(token.getUser().getEmail()); // Utilisation de l'email de l'utilisateur
        dto.setExpiryDate(token.getExpiryDate());
        return dto;
    }

    // Convertir de DTO à l'entité
    public PasswordResetToken toEntity(PasswordResetTokenDTO dto, User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(dto.getToken());
        token.setUser(user); // Assurez-vous de passer l'entité utilisateur
        token.setExpiryDate(dto.getExpiryDate());
        return token;
    }
}
