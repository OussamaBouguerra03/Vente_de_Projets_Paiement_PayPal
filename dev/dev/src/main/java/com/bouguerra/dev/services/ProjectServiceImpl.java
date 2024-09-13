package com.bouguerra.dev.services;

import com.bouguerra.dev.dto.ProjectDTO;
import com.bouguerra.dev.mappers.ProjectMapper;
import com.bouguerra.dev.models.Project;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.ProjectRepository;
import com.bouguerra.dev.repositories.UserRepository;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;
 
    public ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile videoFile) throws Exception {
        try {
            // Logs supplémentaires pour le débogage
            System.out.println("Début de la création du projet avec les détails : " + projectDTO);
    
            User user = userRepository.findById(projectDTO.getUserId())
                    .orElseThrow(() -> {
                        System.out.println("Utilisateur non trouvé pour l'ID : " + projectDTO.getUserId());
                        return new Exception("User not found");
                    });
    
            byte[] videoBytes = convertVideoToBytes(videoFile);
    
            Project project = ProjectMapper.toEntity(projectDTO, user);
            project.setVideo(videoBytes);
    
            Project savedProject = projectRepository.save(project);
    
            return ProjectMapper.toDTO(savedProject);
        } catch (IOException e) {
            System.out.println("Erreur lors de la conversion du fichier vidéo : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid video file", e);
        } catch (Exception e) {
            System.out.println("Erreur lors de la création du projet : " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create project", e);
        }
    }
    private byte[] convertVideoToBytes(MultipartFile videoFile) throws IOException {
        if (videoFile != null && !videoFile.isEmpty()) {
            System.out.println("Le fichier vidéo n'est pas vide. Nom du fichier : " + videoFile.getOriginalFilename());
            return videoFile.getBytes();
        } else {
            throw new IOException("Video file is empty or null");
        }
    }
    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        return ProjectMapper.toDTO(project);
    }

    @Override
    public List<ProjectDTO> getProjectsByUserId(Long userId) {
        return projectRepository.findByUserId(userId).stream()
                .map(ProjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProject(Long id, ProjectDTO projectDTO, MultipartFile videoFile) throws Exception {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setPrice(projectDTO.getPrice());

        if (videoFile != null && !videoFile.isEmpty()) {
            byte[] videoBytes = convertVideoToBytes(videoFile);
            project.setVideo(videoBytes);
        }

        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found");
        }
        projectRepository.deleteById(id);
    }

    @Override
    public byte[] getProjectVideo(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return project.getVideo();
    }

      @Override
      public String getProjectNameById(Long id) {
        Project project = projectRepository.findById(id).orElse(null);
        return (project != null) ? project.getName() : "Unknown"; // Assurez-vous que cela renvoie une chaîne
    }
    

        
}