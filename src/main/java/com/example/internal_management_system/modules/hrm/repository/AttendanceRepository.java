package com.example.internal_management_system.modules.hrm.repository;

import com.example.internal_management_system.modules.hrm.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
