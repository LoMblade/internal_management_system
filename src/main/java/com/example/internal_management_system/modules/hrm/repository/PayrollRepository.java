package com.example.internal_management_system.modules.hrm.repository;

import com.example.internal_management_system.modules.hrm.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
}
