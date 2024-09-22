package com.employeemanagement.service;

import com.employeemanagement.exception.ProjectNotFoundException;
import com.employeemanagement.model.Project;
import com.employeemanagement.repository.ProjectRepository;
import com.employeemanagement.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProjectServiceMockTest {
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService = new ProjectServiceImpl();

    @Test
    public void getAllProjects() {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        when(projectRepository.findAll()).thenReturn(mockProjects);

        assertEquals(mockProjects,projectService.getAllProjects());
    }

    @Test
    public void getProject() {
        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        assertEquals(project,projectService.getProject(1L));
    }

    @Test
    public void getProjectNotFound() {
        ProjectNotFoundException exception = assertThrows(
                ProjectNotFoundException.class,
                () -> projectService.getProject(1L),
                "Project id not found : 1");

        assertEquals("Project id not found : 1",exception.getMessage());
    }

    @Test
    public void deleteProject() {
        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteProject(1L);

        verify(projectRepository,times(1)).deleteById(1L);
    }

    @Test
    public void createProject() {
        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>());

        when(projectRepository.save(project)).thenReturn(project);

        assertEquals(project,projectService.createProject(project));
    }

    @Test
    public void updateProject() {
        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(project)).thenReturn(project);

        assertEquals(project, projectService.updateProject(project, 1L));
    }
}