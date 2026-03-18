package com.smartschoolhub.service;

import com.smartschoolhub.domain.Attendance;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.AttendanceRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.AttendanceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository, StudentRepository studentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
    }

    public Page<Attendance> getAll(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    public Attendance getById(Long id) {
        return attendanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Attendance not found: " + id));
    }

    public List<Attendance> getByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> getByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public Attendance create(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        apply(attendance, request);
        return attendanceRepository.save(attendance);
    }

    public Attendance update(Long id, AttendanceRequest request) {
        Attendance attendance = getById(id);
        apply(attendance, request);
        return attendanceRepository.save(attendance);
    }

    public void delete(Long id) {
        Attendance attendance = getById(id);
        attendanceRepository.delete(attendance);
    }

    private void apply(Attendance attendance, AttendanceRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
        attendance.setStudent(student);
        attendance.setDate(request.getDate());
        attendance.setStatus(request.getStatus());
    }
}
