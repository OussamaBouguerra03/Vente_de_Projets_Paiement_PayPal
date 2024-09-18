package com.bouguerra.dev.repositories;

import com.bouguerra.dev.models.Project;
import com.bouguerra.dev.models.Purchase;

import java.util.List;

 import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
        List<Project> findByUserId(Long userId);

}
