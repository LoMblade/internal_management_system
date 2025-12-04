package com.example.internal_management_system.modules.hrm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Field 'name' trong database - required field
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    private BigDecimal baseSalary;

    @Column(name = "department_id")
    private Long departmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Auto-populate name từ title nếu name chưa được set
        if (name == null || name.isEmpty()) {
            name = title;
        }
        // Auto-populate audit fields
        populateAuditFields(true);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Sync name với title khi update (nếu title thay đổi)
        if (title != null && !title.isEmpty()) {
            name = title;
        }
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
