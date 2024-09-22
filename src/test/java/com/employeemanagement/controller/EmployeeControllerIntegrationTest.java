package com.employeemanagement.controller;

import com.employeemanagement.BaseIntegrationTest;
import com.employeemanagement.EmployeeManagementApplication;
import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmployeeManagementApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class EmployeeControllerIntegrationTest extends BaseIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @Order(1)
    public void addEmployee() {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        Employee employee = new Employee(10001L,"Manbo","Manager","Finance","114514@gmail.com", mockProjects);

        HttpEntity<Employee> entity = new HttpEntity<>(employee,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/employees"), HttpMethod.POST,entity,String.class
        );

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertEquals(HttpStatus.CREATED.value(),response.getStatusCode().value());
        assertTrue(actual.contains("/api/employees"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @Order(2)
    public void updateEmployee() throws JSONException {
        List<Project> mockProjects = new ArrayList<Project>() {{
            add(new Project(1L, "Project Alpha", "Description of Project Alpha", new ArrayList<>()));
            add(new Project(2L, "Project Beta", "Description of Project Beta", new ArrayList<>()));
        }};

        Employee employee = new Employee(10001L,"Manbo","Manager","Finance","114514@gmail.com", mockProjects);

        HttpEntity<Employee> entity = new HttpEntity<>(employee,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/employees/1"),HttpMethod.PUT,entity,String.class
        );

        assertEquals(HttpStatus.OK.value(),response.getStatusCode().value());

        String expected = "{\"id\":1,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"}";

        JSONAssert.assertEquals(expected,response.getBody(),false);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @Order(3)
    public void getEmployee() throws JSONException, JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/employees/1"),
                HttpMethod.GET,entity,String.class
        );

        String expected = "{\"id\":1,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"}";

        JSONAssert.assertEquals(expected,response.getBody(),false);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    @Order(4)
    public void deleteEmployee() {
        Employee employee = restTemplate.getForObject(createURLWithPort("/api/employees/1"),Employee.class);
        assertNotNull(employee);

        HttpEntity<String> entity = new HttpEntity<>(null,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/employees/1"),HttpMethod.DELETE,entity,String.class
        );

        assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCode().value());

        try {
            employee = restTemplate.getForObject("/api/employees/1", Employee.class);
        } catch (EmployeeNotFoundException e) {
            assertEquals("Employee id not found : 1", e.getMessage());
        }
    }
}
