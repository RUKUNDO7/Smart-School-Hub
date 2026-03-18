package com.smartschoolhub.service;

import com.smartschoolhub.domain.Fee;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.FeeRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.FeeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional
public class FeeService {
    private final FeeRepository feeRepository;
    private final StudentRepository studentRepository;

    public FeeService(FeeRepository feeRepository, StudentRepository studentRepository) {
        this.feeRepository = feeRepository;
        this.studentRepository = studentRepository;
    }

    public Page<Fee> getAll(Pageable pageable) {
        return feeRepository.findAll(pageable);
    }

    public Fee getById(Long id) {
        return feeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Fee record not found: " + id));
    }

    public List<Fee> getByStudent(Long studentId) {
        return feeRepository.findByStudentId(studentId);
    }

    public Fee create(FeeRequest request) {
        Fee fee = new Fee();
        apply(fee, request);
        return feeRepository.save(fee);
    }

    public Fee update(Long id, FeeRequest request) {
        Fee fee = getById(id);
        apply(fee, request);
        return feeRepository.save(fee);
    }

    public void delete(Long id) {
        Fee fee = getById(id);
        feeRepository.delete(fee);
    }

    private void apply(Fee fee, FeeRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
        fee.setStudent(student);
        fee.setAmountDue(request.getAmountDue());
        fee.setAmountPaid(request.getAmountPaid());
        fee.setDueDate(request.getDueDate());
        fee.setPaymentStatus(request.getPaymentStatus());
    }
}
