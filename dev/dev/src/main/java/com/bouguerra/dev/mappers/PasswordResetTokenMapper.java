package com.bouguerra.dev.mappers;

 import com.bouguerra.dev.dto.PasswordResetTokenDTO;
import com.bouguerra.dev.models.PasswordResetToken;
import com.bouguerra.dev.models.User;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenMapper {

 
    public PasswordResetTokenDTO toDTO(PasswordResetToken token) {
        PasswordResetTokenDTO dto = new PasswordResetTokenDTO();
        dto.setToken(token.getToken());
        dto.setUserEmail(token.getUser().getEmail());  
        dto.setExpiryDate(token.getExpiryDate());
        return dto;
    }

 
    public PasswordResetToken toEntity(PasswordResetTokenDTO dto, User user) {
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(dto.getToken());
        token.setUser(user);  
        token.setExpiryDate(dto.getExpiryDate());
        return token;
    }
}
