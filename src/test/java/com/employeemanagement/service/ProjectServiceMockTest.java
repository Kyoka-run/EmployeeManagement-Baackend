package com.employeemanagement.service;

import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.exception.ProjectNotFoundException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Project;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.repository.ProjectRepository;
import com.employeemanagement.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProjectServiceMockTest {
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private EmployeeRepository employeeRepository;

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

    @Test
    public void testProjectNotFound() {
        when(projectRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProject(1L);
        });

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.updateProject(new Project(), 1L);
        });

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.deleteProject(1L);
        });
    }

    @Test
    public void updateProjectWithInvalidEmployees() {
        // Setup
        Long projectId = 1L;
        Long invalidEmployeeId = 999L;
        Project existingProject = new Project(projectId, "Project Alpha", "Description", new ArrayList<>());
        Project updateProject = new Project(projectId, "Updated Project", "Updated Description", new ArrayList<>());

        Employee invalidEmployee = new Employee();
        invalidEmployee.setId(invalidEmployeeId);
        updateProject.setEmployees(Arrays.asList(invalidEmployee));

        // Mock repository responses
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(employeeRepository.findById(invalidEmployeeId)).thenReturn(Optional.empty());

        // Test and verify
        assertThrows(EmployeeNotFoundException.class, () -> {
            projectService.updateProject(updateProject, projectId);
        });
    }

    @Test
    public void updateProjectWithExistingEmployees() {
        // Setup
        Long projectId = 1L;
        Long employeeId = 1L;
        Project existingProject = new Project(projectId, "Project Alpha", "Description", new ArrayList<>());
        Project updateProject = new Project(projectId, "Updated Project", "Updated Description", new ArrayList<>());

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setProjects(new ArrayList<>());
        updateProject.setEmployees(Arrays.asList(employee));

        // Mock repository responses
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(projectRepository.save(any(Project.class))).thenReturn(updateProject);

        // Test
        Project result = projectService.updateProject(updateProject, projectId);

        // Verify
        assertEquals("Updated Project", result.getName());
        assertEquals("Updated Description", result.getDescription());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    public void testSearchProject() {
        Long projectId = 1L;
        Project mockProject = new Project();
        mockProject.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(mockProject));

        Optional<Project> result = projectService.searchProject(projectId);

        assertTrue(result.isPresent());
        assertEquals(projectId, result.get().getId());
    }

    @Test
    public void testSearchProjectNotFound() {
        Long projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.searchProject(projectId);
        });
    }
}