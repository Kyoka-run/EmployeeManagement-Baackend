package com.employeemanagement.controller;

import com.employeemanagement.EmployeeManagementApplication;
import com.employeemanagement.exception.ProjectNotFoundException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@SpringBootTest(classes = EmployeeManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class ProjectControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @Order(1)
    public void addProject() {
        List<Employee> emptyEmployees = new ArrayList<>();

        Project project = new Project(1L, "Project Alpha", "Description of Project Alpha", emptyEmployees);

        HttpEntity<Project> entity = new HttpEntity<>(project, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/projects"), HttpMethod.POST, entity, String.class
        );

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());

        List<String> locationHeader = response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);

        String actual = locationHeader.get(0);
        assertTrue(actual.contains("/projects"));
    }


    @Test
    @Order(2)
    public void updateProject() throws JSONException, JsonProcessingException {
        // 动态获取 Project ID
        HttpEntity<String> entityForGet = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> getResponse = restTemplate.exchange(
                createURLWithPort("/projects"), HttpMethod.GET, entityForGet, String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(getResponse.getBody());
        Long projectId = root.get(0).get("id").asLong();

        List<Employee> emptyEmployees = new ArrayList<>();

        Project project = new Project(projectId, "Project Alpha Updated", "Updated Description of Project Alpha", emptyEmployees);

        HttpEntity<Project> entityForUpdate = new HttpEntity<>(project, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/projects/" + projectId), HttpMethod.PUT, entityForUpdate, String.class
        );

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

        String expected = String.format(
                "{\"id\":%d,\"name\":\"Project Alpha Updated\",\"description\":\"Updated Description of Project Alpha\"," +
                        "\"employees\":[]}",
                projectId
        );

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(3)
    public void getProject() throws JSONException, JsonProcessingException {
        // 动态获取 Project ID
        HttpEntity<String> entityForGet = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> getResponse = restTemplate.exchange(
                createURLWithPort("/projects"), HttpMethod.GET, entityForGet, String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(getResponse.getBody());
        Long projectId = root.get(0).get("id").asLong();

        // 发送 GET 请求获取项目信息
        HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/projects/" + projectId), HttpMethod.GET, entity, String.class
        );

        String expected = String.format(
                "{\"id\":%d,\"name\":\"Project Alpha Updated\",\"description\":\"Updated Description of Project Alpha\"," +
                        "\"employees\":[]}",
                projectId
        );

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(4)
    public void deleteProject() throws JsonProcessingException {
        // 动态获取 Project ID
        HttpEntity<String> entityForGet = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> getResponse = restTemplate.exchange(
                createURLWithPort("/projects"), HttpMethod.GET, entityForGet, String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(getResponse.getBody());
        Long projectId = root.get(0).get("id").asLong();

        Project project = restTemplate.getForObject(createURLWithPort("/projects/" + projectId), Project.class);
        assertNotNull(project);

        // 删除项目
        HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/projects/" + projectId), HttpMethod.DELETE, entity, String.class
        );

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());

        // 确认项目删除
        try {
            project = restTemplate.getForObject("/projects/" + projectId, Project.class);
        } catch (ProjectNotFoundException e) {
            assertEquals("Project id not found : " + projectId, e.getMessage());
        }
    }
}
