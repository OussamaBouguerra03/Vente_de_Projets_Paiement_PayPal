package com.bouguerra.dev.controllers;

import com.bouguerra.dev.dto.ProjectDTO;
 import com.bouguerra.dev.services.CustomerService;
import com.bouguerra.dev.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
 import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = customerService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
     @GetMapping("/video/{id}")
    public ResponseEntity<byte[]> getProjectVideo(@PathVariable Long id) {
        try {
            byte[] videoBytes = customerService.getProjectVideo(id);
            return new ResponseEntity<>(videoBytes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
}
 

@GetMapping("/{id}/image")
public ResponseEntity<byte[]> getUserImage(@PathVariable Long id) {
    try {
        byte[] imageBytes = userService.getUserImage(id);
        if (imageBytes == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
  

}