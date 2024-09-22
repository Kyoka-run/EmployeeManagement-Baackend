package com.employeemanagement.exception;

public class EmployeeNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 179856888745840942L;
	
	public EmployeeNotFoundException(Long id) {
	    super("Employee id not found : " + id);
	}
	
	public EmployeeNotFoundException(String message,Long id) {
	    super("Employee id not found : " + id + " TODO " + message);
	}
}
