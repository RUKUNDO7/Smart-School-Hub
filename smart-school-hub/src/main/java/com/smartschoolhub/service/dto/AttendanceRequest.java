package com.smartschoolhub.service.dto;

import com.smartschoolhub.domain.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class AttendanceRequest {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Attendance status is required")
    private AttendanceStatus status;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
