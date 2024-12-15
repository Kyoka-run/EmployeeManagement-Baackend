package com.employeemanagement.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectNotFoundExceptionTest {
    @Test
    public void testExceptionMessages() {
        // Test constructor with id
        Long id = 1L;
        ProjectNotFoundException exception = new ProjectNotFoundException(id);
        assertEquals("Project id not found : " + id, exception.getMessage());

        // Test constructor with message and id
        ProjectNotFoundException exception2 = new ProjectNotFoundException("Test", id);
        assertEquals("Project id not found : " + id + " TODO Test", exception2.getMessage());
    }
}