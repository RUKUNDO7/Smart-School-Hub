package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TeacherEvaluationRequest {
    @NotNull
    private Long teacherId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Double score;

    private String notes;

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
