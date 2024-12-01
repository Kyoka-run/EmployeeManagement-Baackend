package com.employeemanagement.service;

import com.employeemanagement.model.User;

public interface UserService {
    Long registerUser(User user);
    User findByUsername(String username);
    User findById(Long id);
    void save(User user);
}
