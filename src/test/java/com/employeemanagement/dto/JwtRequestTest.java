package com.employeemanagement.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtRequestTest {
    @Test
    public void testJwtRequestMethods() {
        // Test empty constructor and setters
        JwtRequest request = new JwtRequest();
        request.setUsername("testUser");
        request.setPassword("testPass");
        assertEquals("testUser", request.getUsername());
        assertEquals("testPass", request.getPassword());

        // Test parameterized constructor
        JwtRequest request2 = new JwtRequest("user2", "pass2");
        assertEquals("user2", request2.getUsername());
        assertEquals("pass2", request2.getPassword());
    }
}
