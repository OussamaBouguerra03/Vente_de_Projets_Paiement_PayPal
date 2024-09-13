package com.bouguerra.dev.services;

import com.bouguerra.dev.dto.ProjectDTO;
 
import java.util.List;

public interface CustomerService {
    List<ProjectDTO> getAllProjects();
    byte[] getProjectVideo(Long id);
 
}
