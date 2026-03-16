package com.smartschoolhub.service.dto;

import java.util.ArrayList;
import java.util.List;

public class StudentProgress {
    private Long studentId;
    private String studentName;
    private double averageMarks;
    private long attendancePresent;
    private long attendanceAbsent;
    private long attendanceLate;
    private long attendanceExcused;
    private List<GradeItem> grades = new ArrayList<>();

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getAverageMarks() {
        return averageMarks;
    }

    public void setAverageMarks(double averageMarks) {
        this.averageMarks = averageMarks;
    }

    public long getAttendancePresent() {
        return attendancePresent;
    }

    public void setAttendancePresent(long attendancePresent) {
        this.attendancePresent = attendancePresent;
    }

    public long getAttendanceAbsent() {
        return attendanceAbsent;
    }

    public void setAttendanceAbsent(long attendanceAbsent) {
        this.attendanceAbsent = attendanceAbsent;
    }

    public long getAttendanceLate() {
        return attendanceLate;
    }

    public void setAttendanceLate(long attendanceLate) {
        this.attendanceLate = attendanceLate;
    }

    public long getAttendanceExcused() {
        return attendanceExcused;
    }

    public void setAttendanceExcused(long attendanceExcused) {
        this.attendanceExcused = attendanceExcused;
    }

    public List<GradeItem> getGrades() {
        return grades;
    }

    public void setGrades(List<GradeItem> grades) {
        this.grades = grades;
    }

    public static class GradeItem {
        private Long examId;
        private String subjectName;
        private Double marks;

        public Long getExamId() {
            return examId;
        }

        public void setExamId(Long examId) {
            this.examId = examId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public Double getMarks() {
            return marks;
        }

        public void setMarks(Double marks) {
            this.marks = marks;
        }
    }
}
