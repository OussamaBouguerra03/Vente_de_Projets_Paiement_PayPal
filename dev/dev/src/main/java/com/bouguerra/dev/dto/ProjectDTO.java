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
    private byte[] video;
    private Long userId;  
    private String technologies;
    private String category; 

}
