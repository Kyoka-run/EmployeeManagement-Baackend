package com.employeemanagement.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUpdateRequestTest {
    @Test
    public void testPasswordUpdateRequestMethods() {
        // Test empty constructor and setters
        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setOldPassword("oldPass");
        request.setNewPassword("newPass");
        assertEquals("oldPass", request.getOldPassword());
        assertEquals("newPass", request.getNewPassword());

        // Test parameterized constructor
        PasswordUpdateRequest request2 = new PasswordUpdateRequest("newPass2", "oldPass2");
        assertEquals("oldPass2", request2.getOldPassword());
        assertEquals("newPass2", request2.getNewPassword());
    }
}