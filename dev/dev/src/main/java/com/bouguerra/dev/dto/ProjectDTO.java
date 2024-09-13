package com.bouguerra.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private byte[] video; // Assurez-vous de gérer correctement les vidéos pour éviter les grandes charges.
    private Long userId;  // Représente l'ID de l'utilisateur associé au projet.
    private String technologies;
    private String category; // Category of the project

}
