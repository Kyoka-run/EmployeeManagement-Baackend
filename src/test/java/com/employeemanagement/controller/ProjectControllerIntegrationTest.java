package com.employeemanagement.controller;

import com.employeemanagement.BaseIntegrationTest;
import com.employeemanagement.EmployeeManagementApplication;
import com.employeemanagement.exception.ProjectNotFoundException;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmployeeManagementApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ProjectControllerIntegrationTest extends BaseIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    @Order(1)
    public void addProject() {
        List<Employee> mockEmployees = new ArrayList<Employee>() {{
            add(new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>()));
            add(new Employee(10002L, "Lisi", "Developer", "IT", "lisi@example.com", new ArrayList<>()));
        }};

        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", mockEmployees);

        HttpEntity<Project> entity = new HttpEntity<>(project,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/projects"), HttpMethod.POST,entity,String.class
        );

        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

        assertEquals(HttpStatus.CREATED.value(),response.getStatusCode().value());
        assertTrue(actual.contains("/api/projects"));
    }

    @Test
    @Order(2)
    public void updateProject() throws JSONException {
        List<Employee> mockEmployees = new ArrayList<Employee>() {{
            add(new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>()));
            add(new Employee(10002L, "Lisi", "Developer", "IT", "lisi@example.com", new ArrayList<>()));
        }};

        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", mockEmployees);

        HttpEntity<Project> entity = new HttpEntity<>(project,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/projects/1"),HttpMethod.PUT,entity,String.class
        );

        assertEquals(HttpStatus.OK.value(),response.getStatusCode().value());

        String expected = "{"
                + "\"id\":1,"
                + "\"name\":\"Project Alpha\","
                + "\"describe\":\"Description of Project Alpha\","
                + "\"employees\":["
                + "{\"id\":10001,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"},"
                + "{\"id\":10002,\"name\":\"Lisi\",\"position\":\"Developer\",\"department\":\"IT\",\"email\":\"lisi@example.com\"}"
                + "]"
                + "}";

        JSONAssert.assertEquals(expected,response.getBody(),false);
    }

    @Test
    @Order(3)
    public void getProject() throws JSONException, JsonProcessingException {
        HttpEntity<String> entity = new HttpEntity<>(null,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/projects/1"),
                HttpMethod.GET,entity,String.class
        );

        String expected = "{"
                + "\"id\":1,"
                + "\"name\":\"Project Alpha\","
                + "\"describe\":\"Description of Project Alpha\","
                + "\"employees\":["
                + "{\"id\":10001,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"},"
                + "{\"id\":10002,\"name\":\"Lisi\",\"position\":\"Developer\",\"department\":\"IT\",\"email\":\"lisi@example.com\"}"
                + "]"
                + "}";

        JSONAssert.assertEquals(expected,response.getBody(),false);
    }

    @Test
    @Order(4)
    public void deleteProject() {
        List<Employee> mockEmployees = new ArrayList<Employee>() {{
            add(new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>()));
            add(new Employee(10002L, "Lisi", "Developer", "IT", "lisi@example.com", new ArrayList<>()));
        }};

        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", mockEmployees);

        assertNotNull(project);

        HttpEntity<String> entity = new HttpEntity<>(null,getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/projects/1"),HttpMethod.DELETE,entity,String.class
        );

        assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatusCode().value());

        try {
            project = restTemplate.getForObject("/api/projects/1", Project.class);
        } catch (ProjectNotFoundException e) {
            assertEquals("Employee id not found : 1", e.getMessage());
        }
    }
}
