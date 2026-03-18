package com.smartschoolhub.service;

import com.smartschoolhub.domain.HealthRecord;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.HealthRecordRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.HealthRecordRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
@Transactional
public class HealthRecordService {
    private final HealthRecordRepository healthRecordRepository;
    private final StudentRepository studentRepository;

    public HealthRecordService(HealthRecordRepository healthRecordRepository, StudentRepository studentRepository) {
        this.healthRecordRepository = healthRecordRepository;
        this.studentRepository = studentRepository;
    }

    public List<HealthRecord> getRecordsByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return healthRecordRepository.findByStudentOrderByRecordDateDesc(student);
    }

    @Transactional
    public HealthRecord createRecord(HealthRecordRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        String username = SecurityContextHolder.getContext().getAuthentication() != null ?
            SecurityContextHolder.getContext().getAuthentication().getName() : "SYSTEM";

        HealthRecord record = new HealthRecord();
        record.setStudent(student);
        record.setRecordDate(request.getRecordDate());
        record.setRecordType(request.getRecordType());
        record.setDescription(request.getDescription());
        record.setActionTaken(request.getActionTaken());
        record.setRecordedBy(username);

        return healthRecordRepository.save(record);
    }
}
