package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CurriculumRequest {
    @NotNull(message = "School Class ID is required")
    private Long schoolClassId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Periods per week is required")
    @Min(value = 1, message = "Periods per week must be at least 1")
    private Integer periodsPerWeek;

    public Long getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(Long schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getPeriodsPerWeek() {
        return periodsPerWeek;
    }

    public void setPeriodsPerWeek(Integer periodsPerWeek) {
        this.periodsPerWeek = periodsPerWeek;
    }
}
