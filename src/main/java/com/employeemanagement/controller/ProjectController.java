package com.employeemanagement.controller;

import java.net.URI;
import java.util.List;
import com.employeemanagement.model.Project;
import com.employeemanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects/{id}")
    public Project getProject(@PathVariable Long id) {
        Project project = projectService.getProject(id);
        return project;
    }

    @GetMapping("/projects")
    public List<Project> getAllEmployees() {
        List<Project> projects = projectService.getAllProjects();
        return projects;
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {
        projectService.deleteProject(id);
        ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
        return responseEntity;
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable long id,@RequestBody Project project) {
        Project updatedProject = projectService.updateProject(project,id);
        ResponseEntity<Project> responseEntity = new ResponseEntity<Project>(updatedProject,HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping("/projects")
    public ResponseEntity<Void> createEmployee(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        if(createdProject == null) return ResponseEntity.noContent().build();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdProject.getId())
                .toUri();
        return ResponseEntity.created(uri).build();
    }
}

