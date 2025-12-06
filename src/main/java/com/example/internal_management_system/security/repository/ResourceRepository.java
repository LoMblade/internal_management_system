package com.example.internal_management_system.security.repository;

import com.example.internal_management_system.security.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    Optional<Resource> findByCode(String code);
    List<Resource> findByModule_Code(String moduleCode);
    boolean existsByCode(String code);
}


