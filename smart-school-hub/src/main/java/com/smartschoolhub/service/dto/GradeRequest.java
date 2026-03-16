package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotNull;

public class GradeRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long examId;

    @NotNull
    private Double marks;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public Double getMarks() {
        return marks;
    }

    public void setMarks(Double marks) {
        this.marks = marks;
    }
}
