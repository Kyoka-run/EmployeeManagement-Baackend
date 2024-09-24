package com.employeemanagement.controller;

import com.employeemanagement.exception.EmployeeNotFoundException;
import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.*;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class EmployeeControllerIntegrationTest {
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
    public void addEmployee() {
        List<Project> mockProjects = new ArrayList<>();

        Employee employee = new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", mockProjects);

        HttpEntity<Employee> entity = new HttpEntity<>(employee, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/employees"), HttpMethod.POST, entity, String.class
        );

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());

        List<String> locationHeader = response.getHeaders().get(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);

        String actual = locationHeader.get(0);
        assertTrue(actual.contains("/employees"));
    }

    @Test
    @Order(2)
    public void updateEmployee() throws JSONException, JsonProcessingException {
        HttpEntity<String> entityForGet = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> getResponse = restTemplate.exchange(
                createURLWithPort("/employees"), HttpMethod.GET, entityForGet, String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(getResponse.getBody());
        Long employeeId = root.get(0).get("id").asLong();

        List<Project> mockProjects = new ArrayList<>();

        Employee employee = new Employee(employeeId, "Manbo", "Manager", "Finance", "114514@gmail.com", mockProjects);

        HttpEntity<Employee> entityForUpdate = new HttpEntity<>(employee, getHttpHeader());

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/employees/" + employeeId), HttpMethod.PUT, entityForUpdate, String.class
        );

        assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());

        String expected = String.format(
                "{\"id\":%d,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"}",
                employeeId
        );

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }


    @Test
    @Order(3)
    public void getEmployee() throws JSONException, JsonProcessingException {
        HttpEntity<String> entityForGet = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> getResponse = restTemplate.exchange(
                createURLWithPort("/employees"), HttpMethod.GET, entityForGet, String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(getResponse.getBody());
        Long employeeId = root.get(0).get("id").asLong();

        HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/employees/" + employeeId), HttpMethod.GET, entity, String.class
        );

        String expected = String.format(
                "{\"id\":%d,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"}",
                employeeId
        );

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    @Order(4)
    public void deleteEmployee() throws JsonProcessingException {
        HttpEntity<String> entityForGet = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> getResponse = restTemplate.exchange(
                createURLWithPort("/employees"), HttpMethod.GET, entityForGet, String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(getResponse.getBody());
        Long employeeId = root.get(0).get("id").asLong();

        Employee employee = restTemplate.getForObject(createURLWithPort("/employees/" + employeeId), Employee.class);
        assertNotNull(employee);

        HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/employees/" + employeeId), HttpMethod.DELETE, entity, String.class
        );

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());

        try {
            employee = restTemplate.getForObject("/employees/" + employeeId, Employee.class);
        } catch (EmployeeNotFoundException e) {
            assertEquals("Employee id not found : " + employeeId, e.getMessage());
        }
    }
}

