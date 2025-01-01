package org.jl.projectmanagmentsystem.controller;

import org.jl.projectmanagmentsystem.model.Project;
import org.jl.projectmanagmentsystem.repository.ProjectRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger logger = LogManager.getLogger(ProjectController.class);

    @Autowired
    private ProjectRepository projectRepository;

    // Получить все проекты
    @GetMapping
    public List<Project> getAllProjects() {
        logger.info("Fetching all projects");
        List<Project> projects = projectRepository.findAll();
        logger.info("Found {} projects", projects.size());
        return projects;
    }

    // Получить проект по ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        logger.info("Fetching project with ID: {}", id);
        Optional<Project> project = projectRepository.findById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Project with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Создать новый проект
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        logger.info("Creating new project: {}", project.getName());
        Project savedProject = projectRepository.save(project);
        logger.info("Project created with ID: {}", savedProject.getId());
        return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
    }

    // Обновить проект
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails) {
        logger.info("Updating project with ID: {}", id);
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            Project updatedProject = projectRepository.save(project);
            logger.info("Project with ID: {} updated successfully", id);
            return ResponseEntity.ok(updatedProject);
        } else {
            logger.warn("Project with ID: {} not found for update", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Удалить проект
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        logger.info("Deleting project with ID: {}", id);
        projectRepository.deleteById(id);
        logger.info("Project with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}
