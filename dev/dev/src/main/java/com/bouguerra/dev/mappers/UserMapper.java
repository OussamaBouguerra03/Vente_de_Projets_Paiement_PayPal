package com.bouguerra.dev.mappers;
 
 import org.springframework.stereotype.Component;
 
import com.bouguerra.dev.dto.UserDTO;
 import com.bouguerra.dev.models.User;
@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
         dto.setPicture(user.getPicture());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
         user.setPicture(dto.getPicture());
        return user;
    }
}
