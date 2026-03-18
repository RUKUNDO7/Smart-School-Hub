package com.smartschoolhub.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "salary_components")
public class SalaryComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @Column(nullable = false)
    private String name; // e.g., Base Salary, Housing Allowance, Tax Deduction

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "is_deduction", nullable = false)
    private boolean isDeduction = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isDeduction() {
        return isDeduction;
    }

    public void setDeduction(boolean deduction) {
        isDeduction = deduction;
    }
}
