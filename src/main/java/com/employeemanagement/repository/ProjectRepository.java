package com.employeemanagement.repository;
import java.util.List;

import com.employeemanagement.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByName(String name);
}
