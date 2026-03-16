package com.smartschoolhub.service.dto;

public class DashboardSummary {
    private long studentCount;
    private long teacherCount;
    private long classCount;
    private long subjectCount;
    private long examCount;
    private double totalFeesDue;
    private double totalFeesPaid;

    public long getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(long studentCount) {
        this.studentCount = studentCount;
    }

    public long getTeacherCount() {
        return teacherCount;
    }

    public void setTeacherCount(long teacherCount) {
        this.teacherCount = teacherCount;
    }

    public long getClassCount() {
        return classCount;
    }

    public void setClassCount(long classCount) {
        this.classCount = classCount;
    }

    public long getSubjectCount() {
        return subjectCount;
    }

    public void setSubjectCount(long subjectCount) {
        this.subjectCount = subjectCount;
    }

    public long getExamCount() {
        return examCount;
    }

    public void setExamCount(long examCount) {
        this.examCount = examCount;
    }

    public double getTotalFeesDue() {
        return totalFeesDue;
    }

    public void setTotalFeesDue(double totalFeesDue) {
        this.totalFeesDue = totalFeesDue;
    }

    public double getTotalFeesPaid() {
        return totalFeesPaid;
    }

    public void setTotalFeesPaid(double totalFeesPaid) {
        this.totalFeesPaid = totalFeesPaid;
    }
}
