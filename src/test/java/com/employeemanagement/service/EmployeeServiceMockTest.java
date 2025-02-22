package com.employeemanagement.service;

import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Project;
import com.employeemanagement.repository.EmployeeRepository;
import com.employeemanagement.service.impl.EmployeeServiceImpl;
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
public class EmployeeServiceMockTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService = new EmployeeServiceImpl();

    @Test
    public void getAllEmployees() {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        List<Employee> employees = Arrays.asList(
            new Employee(10001L,"Manbo","Manager","Finance","114514@gmail.com", new ArrayList<>()),
            new Employee(10002L,"Manbo","Manager","Human Resources","114515@gmail.com", new ArrayList<>())
        );

        when(employeeRepository.findAll()).thenReturn(employees);

        assertEquals(employees,employeeService.getAllEmployees());
    }

    @Test
    public void getEmployee() {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        Employee employee = new Employee(10001L,"Manbo","Manager","Finance","114514@gmail.com", mockProjects);

        when(employeeRepository.findById(10001L)).thenReturn(Optional.of(employee));

        assertEquals(employee,employeeService.getEmployee(10001L));
    }

    @Test
    public void getEmployeeNotFound() {
        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployee(10001L),
                "Employee id not found : 10001");

        assertEquals("Employee id not found : 10001",exception.getMessage());
    }

    @Test
    public void deleteEmployee() {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        Employee employee = new Employee(10001L,"Manbo","Manager","Finance","114514@gmail.com", mockProjects);

        when(employeeRepository.findById(10001L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(10001L);

        verify(employeeRepository,times(1)).deleteById(10001L);
    }

    @Test
    public void createEmployee() {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        Employee employee = new Employee(10001L,"Manbo","Manager","Finance","114514@gmail.com", mockProjects);

        when(employeeRepository.save(employee)).thenReturn(employee);

        assertEquals(employee,employeeService.createEmployee(employee));
    }

    @Test
    public void updateEmployee() {
        Employee employee = new Employee(1L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>());

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        assertEquals(employee, employeeService.updateEmployee(employee, 1L));
    }

    @Test
    public void testEmployeeNotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployee(1L);
        });

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployee(new Employee(), 1L);
        });

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.deleteEmployee(1L);
        });
    }

    @Test
    public void testSearchEmployee() {
        Long employeeId = 1L;
        Employee mockEmployee = new Employee();
        mockEmployee.setId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(mockEmployee));

        Optional<Employee> result = employeeService.searchEmployee(employeeId);

        assertTrue(result.isPresent());
        assertEquals(employeeId, result.get().getId());
    }

    @Test
    public void testSearchEmployeeNotFound() {
        Long employeeId = 999L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.searchEmployee(employeeId);
        });
    }
}
