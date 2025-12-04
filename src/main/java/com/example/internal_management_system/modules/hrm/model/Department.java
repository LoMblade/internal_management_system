<<<<<<< Current (Your changes)
package com.example.internal_management_system.modules.hrm.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Auto-populate audit fields
        populateAuditFields(true);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Auto-populate audit fields
        populateAuditFields(false);
    }

    /**
     * Auto-populate audit fields từ SecurityContext
     */
    private void populateAuditFields(boolean isCreate) {
        try {
            // Lấy username từ SecurityContext
            String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

            if (isCreate) {
                this.createdBy = currentUsername;
            }
            this.updatedBy = currentUsername;
        } catch (Exception e) {
            // Nếu không có authentication context (ví dụ trong tests), set default values
            if (isCreate) {
                this.createdBy = "system";
            }
            this.updatedBy = "system";
        }
    }
}
=======
 
>>>>>>> Incoming (Background Agent changes)
