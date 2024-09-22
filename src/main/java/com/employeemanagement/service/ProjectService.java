package com.employeemanagement.service;

import com.employeemanagement.model.Project;

import java.util.List;
import java.util.Optional;


public interface ProjectService {
    List<Project> getAllProjects();
    Project getProject(Long id);
    void deleteProject(Long id);
    Project createProject(Project project);
    Project updateProject(Project project,Long id);
    Optional<Project> searchProject(Long id);
}


