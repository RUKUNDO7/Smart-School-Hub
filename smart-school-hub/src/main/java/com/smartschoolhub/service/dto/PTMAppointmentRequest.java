package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotNull;

public class PTMAppointmentRequest {
    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private String parentNote;

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getParentNote() {
        return parentNote;
    }

    public void setParentNote(String parentNote) {
        this.parentNote = parentNote;
    }
}
