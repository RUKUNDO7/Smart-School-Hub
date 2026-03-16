package com.smartschoolhub.service.dto;

import com.smartschoolhub.domain.PaymentStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class FeeRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Double amountDue;

    @NotNull
    private Double amountPaid;

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

    public Double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Double amountDue) {
        this.amountDue = amountDue;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
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
