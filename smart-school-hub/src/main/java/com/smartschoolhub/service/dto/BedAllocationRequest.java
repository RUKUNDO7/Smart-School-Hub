package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class BedAllocationRequest {
    @NotNull
    private Long roomId;

    @NotNull
    private Long studentId;

    @NotNull
    private LocalDate allocationDate;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public LocalDate getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(LocalDate allocationDate) {
        this.allocationDate = allocationDate;
    }
}
