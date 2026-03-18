package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class SalaryComponentRequest {
    @NotNull(message = "Teacher ID is required")
    private Long teacherId;

    @NotBlank(message = "Component name is required")
    private String name;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", message = "Amount must be non-negative")
    private BigDecimal amount;

    private boolean isDeduction;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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
