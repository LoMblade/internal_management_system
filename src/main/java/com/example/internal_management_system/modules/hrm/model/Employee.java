package com.example.internal_management_system.modules.hrm.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Mã nhân viên duy nhất */
    @Column(name = "employee_code", nullable = false, unique = true, length = 20)
    @NotBlank
    private String employeeCode;

    /** Code nội bộ cho doanh nghiệp (nếu không dùng có thể bỏ) */
    @Column(name = "code", nullable = false, length = 20)
    @NotBlank
    private String code;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(length = 20)
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(length = 200)
    private String address;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    /** Chỉ lưu ID cho phép FE truyền trực tiếp */
    @Column(name = "department_id")
    private Long departmentId;

    /** Relationship read-only */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @Column(name = "position_id")
    private Long positionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private Position position;

    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EmployeeStatus status;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /** Trạng thái nhân viên */
    public enum EmployeeStatus {
        ACTIVE, INACTIVE, TERMINATED, ON_LEAVE
    }

    /** Khi tạo mới */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null)
            this.status = EmployeeStatus.ACTIVE;

        if (this.hireDate == null)
            this.hireDate = LocalDate.now();

        populateAuditFields(true);
    }

    /** Khi update */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        populateAuditFields(false);
    }

    /**
     * Lấy thông tin user từ SecurityContext và set vào createdBy/updatedBy
     */
    private void populateAuditFields(boolean isCreate) {
        String username = "system";

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated()) {
                username = auth.getName();
            }
        } catch (Exception ignored) {}

        if (isCreate) {
            this.createdBy = username;
        }

        this.updatedBy = username;
    }
}
