package com.employeemanagement.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JwtResponseTest {
    @Test
    public void testJwtResponseMethods() {
        // Test constructor and getters
        JwtResponse response = new JwtResponse("token123", 1L);
        assertEquals("token123", response.getToken());
        assertEquals(1L, response.getUserId());

        // Test setter
        response.setToken("newToken");
        assertEquals("newToken", response.getToken());
    }
}
