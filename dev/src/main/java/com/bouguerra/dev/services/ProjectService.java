package com.bouguerra.dev.services;

import com.bouguerra.dev.dto.ProjectDTO;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
     ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile videoFile) throws Exception;
      // Récupérer tous les projets
    List<ProjectDTO> getAllProjects();

    // Récupérer un projet par ID
    ProjectDTO getProjectById(Long id);

    // Récupérer tous les projets d'un utilisateur par ID
    List<ProjectDTO> getProjectsByUserId(Long userId);

    // Mettre à jour un projet existant
    void updateProject(Long id, ProjectDTO projectDTO, MultipartFile videoFile) throws Exception;

    // Supprimer un projet par ID
    void deleteProject(Long id);

    // Récupérer la vidéo d'un projet par ID
    byte[] getProjectVideo(Long id);
    String getProjectNameById(Long id);

}