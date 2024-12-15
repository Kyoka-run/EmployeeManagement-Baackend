package com.employeemanagement.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeNotFoundExceptionTest {
    @Test
    public void testExceptionMessages() {
        // Test constructor with id
        Long id = 1L;
        EmployeeNotFoundException exception = new EmployeeNotFoundException(id);
        assertEquals("Employee id not found : " + id, exception.getMessage());

        // Test constructor with message and id
        EmployeeNotFoundException exception2 = new EmployeeNotFoundException("Test", id);
        assertEquals("Employee id not found : " + id + " TODO Test", exception2.getMessage());
    }
}
