package com.employeemanagement.controller;

import com.employeemanagement.dto.JwtRequest;
import com.employeemanagement.dto.PasswordUpdateRequest;
import com.employeemanagement.model.User;
import com.employeemanagement.security.JwtUtils;
import com.employeemanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldRegisterNewUser() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");
        user.setRoles(Collections.singletonList("USER"));

        when(userService.registerUser(any(User.class))).thenReturn(1L);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully."));
    }

    @Test
    void shouldHandleRegisterExistingUser() throws Exception {
        User user = new User();
        user.setUsername("existingUser");
        user.setPassword("password");

        when(userService.findByUsername("existingUser")).thenReturn(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {
        JwtRequest request = new JwtRequest("testUser", "password");
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("testUser")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
        User user = new User();
        user.setId(1L);

        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null));
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
        when(jwtUtils.generateToken(userDetails)).thenReturn("token");
        when(userService.findByUsername("testUser")).thenReturn(user);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void shouldUpdatePasswordSuccessfully() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");

        when(userService.findById(1L)).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");

        PasswordUpdateRequest request = new PasswordUpdateRequest("newPassword", "oldPassword");

        mockMvc.perform(put("/users/1/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userService).save(argThat(savedUser ->
                savedUser.getId().equals(1L) &&
                        savedUser.getPassword().equals("newHashedPassword")
        ));
    }

    @Test
    void shouldGetUserRoles() throws Exception {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "testUser",
                null,
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        mockMvc.perform(get("/roles")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("ROLE_USER"));
    }

    @Test
    void shouldGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void shouldHandleAuthenticationFailure() throws Exception {
        JwtRequest request = new JwtRequest("testUser", "wrongPassword");

        // Mock authentication failure
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void shouldHandleDisabledUser() throws Exception {
        JwtRequest request = new JwtRequest("disabledUser", "password");

        // Mock disabled user scenario
        when(authenticationManager.authenticate(any()))
                .thenThrow(new DisabledException("User is disabled"));

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void shouldHandleInvalidOldPassword() throws Exception {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setPassword("hashedOldPassword");

        when(userService.findById(userId)).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        PasswordUpdateRequest request = new PasswordUpdateRequest("newPassword", "wrongOldPassword");

        mockMvc.perform(put("/users/" + userId + "/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Old password is incorrect"));
    }

    @Test
    void shouldHandleUserNotFoundForPasswordUpdate() throws Exception {
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(null);

        PasswordUpdateRequest request = new PasswordUpdateRequest("newPassword", "oldPassword");

        mockMvc.perform(put("/users/" + userId + "/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldHandleUserNotFoundById() throws Exception {
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(null);

        mockMvc.perform(get("/users/" + userId))
                .andExpect(status().isNotFound());
    }
}