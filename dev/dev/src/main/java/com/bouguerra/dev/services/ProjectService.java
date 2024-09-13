package com.bouguerra.dev.services;

import com.bouguerra.dev.dto.ProjectDTO;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {
     ProjectDTO createProject(ProjectDTO projectDTO, MultipartFile videoFile) throws Exception;
 
     List<ProjectDTO> getAllProjects();

     ProjectDTO getProjectById(Long id);

     List<ProjectDTO> getProjectsByUserId(Long userId);

     void updateProject(Long id, ProjectDTO projectDTO, MultipartFile videoFile) throws Exception;

     void deleteProject(Long id);

    byte[] getProjectVideo(Long id);

    String getProjectNameById(Long id);

}