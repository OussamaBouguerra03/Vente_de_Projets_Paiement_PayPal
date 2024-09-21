package com.bouguerra.dev.dto;

import com.bouguerra.dev.models.Role;
import com.bouguerra.dev.models.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private byte[] picture;
    private Role role;
    private String errorMessage;  
    private String token;  

     public UserDTO(User user, String token) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.token = token;
    }

     public UserDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public UserDTO(Long id, String username, byte[] picture) {
        this.id = id;
        this.username = username;
        this.picture = picture;
    }

    public UserDTO(Long id, String username, String email, byte[] picture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.picture = picture;
    }
}
