package com.employeemanagement.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.employeemanagement.model.Employee;

//entity,main key type
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	List<Employee> findByName(String name);
}
