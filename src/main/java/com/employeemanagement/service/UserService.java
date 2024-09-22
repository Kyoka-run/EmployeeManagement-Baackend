package com.employeemanagement.service;

import com.employeemanagement.model.User;

public interface UserService {
    Integer registerUser(User user);
    User findByUsername(String username);
}
