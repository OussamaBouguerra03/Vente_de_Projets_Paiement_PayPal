package com.bouguerra.dev.mappers;

import com.bouguerra.dev.dto.ProjectDTO;
import com.bouguerra.dev.models.Project;
import com.bouguerra.dev.models.User;

public class ProjectMapper {

 
    public static ProjectDTO toDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getPrice(),
                project.getVideo(),
                project.getUser().getId(),
                project.getTechnologies(),  
                project.getCategory()  
        );
    }

 
    public static Project toEntity(ProjectDTO projectDTO, User user) {
        return new Project(
                projectDTO.getId(),
                projectDTO.getName(),
                projectDTO.getDescription(),
                projectDTO.getPrice(),
                projectDTO.getTechnologies(), 
                projectDTO.getCategory(),  
                projectDTO.getVideo(),
                user
        );
    }
}
