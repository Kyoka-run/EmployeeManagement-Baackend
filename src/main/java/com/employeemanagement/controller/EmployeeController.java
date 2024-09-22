package com.employeemanagement.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.employeemanagement.model.Employee;
import com.employeemanagement.service.EmployeeService;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping("/employees/{id}")
	public Employee getEmployee(@PathVariable Long id) {
		Employee employee = employeeService.getEmployee(id);
		return employee;
	}
	
	@GetMapping("/employees")
	public List<Employee> getAllEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		return employees;
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable long id) {
		employeeService.deleteEmployee(id);
		ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
		return responseEntity;
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable long id,@RequestBody Employee employee) {
		Employee updatedEmployee = employeeService.updateEmployee(employee,id);
		ResponseEntity<Employee> responseEntity = new ResponseEntity<Employee>(updatedEmployee,HttpStatus.OK);
		return responseEntity;
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Void> createEmployee(@RequestBody Employee employee) {
		Employee createdEmployee = employeeService.createEmployee(employee);
		if(createdEmployee == null) return ResponseEntity.noContent().build();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdEmployee.getId())
				.toUri(); 
		return ResponseEntity.created(uri).build();
	}
}
