package com.example.internal_management_system.modules.hrm.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "pay_period", nullable = false)
    private YearMonth payPeriod;

    private BigDecimal baseSalary;

    private Double overtimeHours;

    private BigDecimal overtimeRate;

    private BigDecimal bonus;

    private BigDecimal deductions;

    private BigDecimal taxAmount;

    private BigDecimal netSalary;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus status;

    @Column(length = 500)
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum PaymentStatus {
        PENDING, PROCESSED, PAID, CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
        // Calculate net salary
        calculateNetSalary();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Recalculate net salary when values change
        calculateNetSalary();
    }

    private void calculateNetSalary() {
        BigDecimal overtimePay = BigDecimal.ZERO;
        if (overtimeHours != null && overtimeRate != null) {
            overtimePay = BigDecimal.valueOf(overtimeHours).multiply(overtimeRate);
        }

        BigDecimal totalEarnings = (baseSalary != null ? baseSalary : BigDecimal.ZERO)
                .add(overtimePay)
                .add(bonus != null ? bonus : BigDecimal.ZERO);

        BigDecimal totalDeductions = (deductions != null ? deductions : BigDecimal.ZERO)
                .add(taxAmount != null ? taxAmount : BigDecimal.ZERO);

        this.netSalary = totalEarnings.subtract(totalDeductions);
    }
}
