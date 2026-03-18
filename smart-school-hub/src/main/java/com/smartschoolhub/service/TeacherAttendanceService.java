package com.smartschoolhub.service;

import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.domain.TeacherAttendance;
import com.smartschoolhub.repository.TeacherAttendanceRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.TeacherAttendanceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TeacherAttendanceService {
    private final TeacherAttendanceRepository teacherAttendanceRepository;
    private final TeacherRepository teacherRepository;

    public TeacherAttendanceService(TeacherAttendanceRepository teacherAttendanceRepository, TeacherRepository teacherRepository) {
        this.teacherAttendanceRepository = teacherAttendanceRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherAttendance> getAll() {
        return teacherAttendanceRepository.findAll();
    }

    public TeacherAttendance getById(Long id) {
        return teacherAttendanceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher attendance not found: " + id));
    }

    public List<TeacherAttendance> getByTeacher(Long teacherId) {
        return teacherAttendanceRepository.findByTeacherId(teacherId);
    }

    public List<TeacherAttendance> getByDate(LocalDate date) {
        return teacherAttendanceRepository.findByDate(date);
    }

    public TeacherAttendance create(TeacherAttendanceRequest request) {
        TeacherAttendance attendance = new TeacherAttendance();
        apply(attendance, request);
        return teacherAttendanceRepository.save(attendance);
    }

    public TeacherAttendance update(Long id, TeacherAttendanceRequest request) {
        TeacherAttendance attendance = getById(id);
        apply(attendance, request);
        return teacherAttendanceRepository.save(attendance);
    }

    public void delete(Long id) {
        TeacherAttendance attendance = getById(id);
        teacherAttendanceRepository.delete(attendance);
    }

    private void apply(TeacherAttendance attendance, TeacherAttendanceRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));
        attendance.setTeacher(teacher);
        attendance.setDate(request.getDate());
        attendance.setStatus(request.getStatus());
    }
}
