package com.bouguerra.dev.mappers;

import com.bouguerra.dev.dto.ProjectDTO;
import com.bouguerra.dev.models.Project;
import com.bouguerra.dev.models.User;

public class ProjectMapper {

    // Convertir Project en ProjectDTO
    public static ProjectDTO toDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getPrice(),
                project.getVideo(),
                project.getUser().getId(),
                project.getTechnologies(), // Ajouter les technologies
                project.getCategory() // Ajouter la catégorie
        );
    }

    // Convertir ProjectDTO en Project
    public static Project toEntity(ProjectDTO projectDTO, User user) {
        return new Project(
                projectDTO.getId(),
                projectDTO.getName(),
                projectDTO.getDescription(),
                projectDTO.getPrice(),
                projectDTO.getTechnologies(), // Ajouter les technologies
                projectDTO.getCategory(), // Ajouter la catégorie
                projectDTO.getVideo(),
                user
        );
    }
}
