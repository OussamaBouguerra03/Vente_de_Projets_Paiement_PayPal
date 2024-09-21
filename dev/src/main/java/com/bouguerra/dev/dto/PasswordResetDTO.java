package com.bouguerra.dev.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordResetDTO {
    @NotBlank(message = "Le mot de passe ne peut pas être nul ou vide.")
    private String password;

    @NotBlank(message = "La confirmation du mot de passe ne peut pas être nulle ou vide.")
    private String confirmPassword;
     public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
