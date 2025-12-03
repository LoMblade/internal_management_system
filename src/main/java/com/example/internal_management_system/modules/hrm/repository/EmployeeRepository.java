package com.example.internal_management_system.modules.hrm.repository;

import com.example.internal_management_system.modules.hrm.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
