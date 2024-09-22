package com.employeemanagement.controller;

import com.employeemanagement.model.Employee;
import com.employeemanagement.model.Project;
import com.employeemanagement.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ProjectControllerMockTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private static final ObjectMapper om = new ObjectMapper();

    List<Employee> mockEmployees = new ArrayList<Employee>() {{
        add(new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>()));
        add(new Employee(10002L, "Lisi", "Developer", "IT", "lisi@example.com", new ArrayList<>()));
    }};

    Project mockProject = new Project(1L, "Project Alpha", "Description of Project Alpha", mockEmployees);

    String exampleProjectJson = "{"
            + "\"id\":1,"
            + "\"name\":\"Project Alpha\","
            + "\"describe\":\"Description of Project Alpha\","
            + "\"employees\":["
            + "{\"id\":10001,\"name\":\"Manbo\",\"position\":\"Manager\",\"department\":\"Finance\",\"email\":\"114514@gmail.com\"},"
            + "{\"id\":10002,\"name\":\"Lisi\",\"position\":\"Developer\",\"department\":\"IT\",\"email\":\"lisi@example.com\"}"
            + "]"
            + "}";

    @Test
    public void getProejct() throws Exception {
        Mockito.when(projectService.getProject(1L)).thenReturn(mockProject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/projects/{id}",1L)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(exampleProjectJson,result.getResponse().getContentAsString(),false);
    }

    @Test
    public void createProject() throws Exception {
        List<Employee> mockEmployees = new ArrayList<Employee>() {{
            add(new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>()));
            add(new Employee(10002L, "Lisi", "Developer", "IT", "lisi@example.com", new ArrayList<>()));
        }};

        Project mockProject = new Project(1L, "Project Alpha", "Description of Project Alpha", mockEmployees);

        Mockito.when(projectService.createProject(Mockito.any(Project.class))).thenReturn(mockProject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/projects").content(exampleProjectJson).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(),response.getStatus());
        assertEquals("http://localhost/projects/1",response.getHeader(HttpHeaders.LOCATION));
    }

    @Test
    public void updateProject() throws Exception {
        List<Employee> mockEmployees = new ArrayList<Employee>() {{
            add(new Employee(10001L, "Manbo", "Manager", "Finance", "114514@gmail.com", new ArrayList<>()));
            add(new Employee(10002L, "Lisi", "Developer", "IT", "lisi@example.com", new ArrayList<>()));
        }};

        Project mockProject = new Project(1L, "Project Alpha", "Description of Project Alpha", mockEmployees);

        Mockito.when(projectService
                        .updateProject(Mockito.any(Project.class),Mockito.anyLong()))
                .thenReturn(mockProject);

        String projectString = om.writeValueAsString(mockProject);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/employees/10001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectString);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(),response.getStatus());
        JSONAssert.assertEquals(exampleProjectJson, response.getContentAsString(),false);
    }

    @Test
    public void deleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(1L);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/project/1");

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(),response.getStatus());
    }
}
