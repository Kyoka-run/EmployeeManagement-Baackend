package com.employeemanagement.exception;

public class ProjectNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 179856888745840943L;

    public ProjectNotFoundException(Long id) {
        super("Project id not found : " + id);
    }

    public ProjectNotFoundException(String message,Long id) {
        super("Project id not found : " + id + " TODO " + message);
    }
}

