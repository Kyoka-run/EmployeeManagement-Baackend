package com.employeemanagement.controller;

import com.employeemanagement.dto.JwtRequest;
import com.employeemanagement.dto.JwtResponse;
import com.employeemanagement.dto.PasswordUpdateRequest;
import com.employeemanagement.model.User;
import com.employeemanagement.security.JwtUtils;
import com.employeemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{id}/update-password")
    public ResponseEntity<?> updatePassword(@PathVariable Long id, @RequestBody PasswordUpdateRequest request) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Old password is incorrect");
        }

        // update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.save(user);

        return ResponseEntity.ok("Password updated successfully");
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) {
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);

            User user = userService.findByUsername(authenticationRequest.getUsername());
            Long userId = user.getId();

            return ResponseEntity.ok(new JwtResponse(token, userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getUserRoles(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}

