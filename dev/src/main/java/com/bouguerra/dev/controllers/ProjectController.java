package com.bouguerra.dev.controllers;

import com.bouguerra.dev.dto.ProjectDTO;
import com.bouguerra.dev.services.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @PostMapping("/create")
    public ResponseEntity<?> createProject(
            @RequestPart(value = "project", required = false) String projectJson,
            @RequestPart("video") MultipartFile videoFile,
            HttpServletRequest request) { // Ajoutez HttpServletRequest comme paramètre
        System.out.println("Content Type: " + request.getContentType());
        System.out.println("Received project JSON: " + projectJson);
        System.out.println("Received video file: " + (videoFile != null ? videoFile.getOriginalFilename() : "None"));
    
        if (projectJson == null) {
            return new ResponseEntity<>("Project data is missing", HttpStatus.BAD_REQUEST);
        }
        if (videoFile == null || videoFile.isEmpty()) {
            return new ResponseEntity<>("Video file is missing", HttpStatus.BAD_REQUEST);
        }
    
        try {
            // Convertir JSON en ProjectDTO
            ObjectMapper objectMapper = new ObjectMapper();
            ProjectDTO projectDTO = objectMapper.readValue(projectJson, ProjectDTO.class);
    
            // Appel du service pour ajouter un nouveau projet
            ProjectDTO createdProject = projectService.createProject(projectDTO, videoFile);
            return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing video file", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }}
        @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        try {
            ProjectDTO projectDTO = projectService.getProjectById(id);
            return new ResponseEntity<>(projectDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByUserId(@PathVariable Long userId) {
        List<ProjectDTO> projects = projectService.getProjectsByUserId(userId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/video/{id}")
    public ResponseEntity<byte[]> getProjectVideo(@PathVariable Long id) {
        try {
            byte[] videoBytes = projectService.getProjectVideo(id);
            return new ResponseEntity<>(videoBytes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/name")
    public ResponseEntity<String> getProjectNameById(@PathVariable Long id) {
        String projectName = projectService.getProjectNameById(id);
        return ResponseEntity.ok(projectName);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(
            @PathVariable Long id,
            @RequestPart(value = "project") String projectJson,
            @RequestPart(value = "video", required = false) MultipartFile videoFile) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ProjectDTO projectDTO = objectMapper.readValue(projectJson, ProjectDTO.class);
            projectService.updateProject(id, projectDTO, videoFile);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error processing video file", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Project not found", HttpStatus.NOT_FOUND);
        }
    }
    
    }