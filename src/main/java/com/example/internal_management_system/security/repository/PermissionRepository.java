package com.example.internal_management_system.security.repository;

import com.example.internal_management_system.security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByResource_CodeAndCode(String resourceCode, String permissionCode);
    
    @Query("SELECT p FROM Permission p WHERE p.resource.code = :resourceCode")
    List<Permission> findByResourceCode(@Param("resourceCode") String resourceCode);
    
    @Query("SELECT p FROM Permission p WHERE p.resource.module.code = :moduleCode")
    List<Permission> findByModuleCode(@Param("moduleCode") String moduleCode);
}


