package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotNull;

public class SubmissionRequest {
    @NotNull
    private Long assignmentId;

    @NotNull
    private Long studentId;

    private String remarks;

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
