package com.smartschoolhub.service.dto;

import com.smartschoolhub.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

import java.time.LocalDate;

public class FeeRequest {
    @NotNull
    private Long studentId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amountDue;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal amountPaid;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private PaymentStatus paymentStatus;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
