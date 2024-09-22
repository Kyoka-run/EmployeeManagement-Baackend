package com.employeemanagement.repository;
import java.util.List;

import com.employeemanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByName(String name);
}
