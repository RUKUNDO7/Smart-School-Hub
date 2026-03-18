package com.smartschoolhub.service;

import com.smartschoolhub.domain.Attendance;
import com.smartschoolhub.domain.AttendanceStatus;
import com.smartschoolhub.domain.Fee;
import com.smartschoolhub.domain.Grade;
import com.smartschoolhub.domain.TeacherEvaluation;
import com.smartschoolhub.repository.AttendanceRepository;
import com.smartschoolhub.repository.FeeRepository;
import com.smartschoolhub.repository.GradeRepository;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.repository.TeacherEvaluationRepository;
import com.smartschoolhub.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsService {
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final FeeRepository feeRepository;
    private final AttendanceRepository attendanceRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherEvaluationRepository teacherEvaluationRepository;

    public ReportsService(StudentRepository studentRepository,
                          GradeRepository gradeRepository,
                          SchoolClassRepository schoolClassRepository,
                          FeeRepository feeRepository,
                          AttendanceRepository attendanceRepository,
                          TeacherRepository teacherRepository,
                          TeacherEvaluationRepository teacherEvaluationRepository) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.feeRepository = feeRepository;
        this.attendanceRepository = attendanceRepository;
        this.teacherRepository = teacherRepository;
        this.teacherEvaluationRepository = teacherEvaluationRepository;
    }

    public Map<String, Object> studentReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("studentCount", studentRepository.count());
        List<Grade> grades = gradeRepository.findAll();
        double avg = grades.stream().mapToDouble(Grade::getMarks).average().orElse(0.0);
        report.put("averageMarks", avg);
        return report;
    }

    public Map<String, Object> classReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("classCount", schoolClassRepository.count());
        report.put("studentCount", studentRepository.count());
        return report;
    }

    public Map<String, Object> financeReport() {
        Map<String, Object> report = new HashMap<>();
        List<Fee> fees = feeRepository.findAll();
        BigDecimal totalDue = fees.stream()
            .map(fee -> fee.getAmountDue() != null ? fee.getAmountDue() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = fees.stream()
            .map(fee -> fee.getAmountPaid() != null ? fee.getAmountPaid() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        report.put("totalFeesDue", totalDue);
        report.put("totalFeesPaid", totalPaid);
        report.put("outstanding", totalDue.subtract(totalPaid));
        return report;
    }

    public Map<String, Object> attendanceReport() {
        Map<String, Object> report = new HashMap<>();
        List<Attendance> records = attendanceRepository.findAll();
        report.put("present", records.stream().filter(a -> a.getStatus() == AttendanceStatus.PRESENT).count());
        report.put("absent", records.stream().filter(a -> a.getStatus() == AttendanceStatus.ABSENT).count());
        report.put("late", records.stream().filter(a -> a.getStatus() == AttendanceStatus.LATE).count());
        report.put("excused", records.stream().filter(a -> a.getStatus() == AttendanceStatus.EXCUSED).count());
        report.put("total", records.size());
        return report;
    }

    public Map<String, Object> teacherReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("teacherCount", teacherRepository.count());
        List<TeacherEvaluation> evaluations = teacherEvaluationRepository.findAll();
        double avg = evaluations.stream().mapToDouble(TeacherEvaluation::getScore).average().orElse(0.0);
        report.put("averageEvaluationScore", avg);
        return report;
    }
}
