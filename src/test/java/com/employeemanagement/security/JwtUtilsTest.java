package com.employeemanagement.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {
    private JwtUtils jwtUtils;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        // Set secret key using reflection
        try {
            java.lang.reflect.Field field = JwtUtils.class.getDeclaredField("secretKey");
            field.setAccessible(true);
            field.set(jwtUtils, "testSecretKey123");
        } catch (Exception e) {
            e.printStackTrace();
        }
        userDetails = new User("testUser", "pass", new ArrayList<>());
    }

    @Test
    void testTokenOperations() {
        // Generate token
        String token = jwtUtils.generateToken(userDetails);
        assertNotNull(token);

        // Extract username
        assertEquals("testUser", jwtUtils.extractUsername(token));

        // Validate token
        assertTrue(jwtUtils.validateToken(token, userDetails));

        // Check expiration
        assertNotNull(jwtUtils.extractExpiration(token));
        assertFalse(jwtUtils.isTokenExpired(token));
    }
}