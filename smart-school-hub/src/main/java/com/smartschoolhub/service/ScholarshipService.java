package com.smartschoolhub.service;

import com.smartschoolhub.domain.Disbursement;
import com.smartschoolhub.domain.Scholarship;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.DisbursementRepository;
import com.smartschoolhub.repository.ScholarshipRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.ScholarshipRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ScholarshipService {
    private final ScholarshipRepository scholarshipRepository;
    private final DisbursementRepository disbursementRepository;
    private final StudentRepository studentRepository;

    public ScholarshipService(ScholarshipRepository scholarshipRepository,
                              DisbursementRepository disbursementRepository,
                              StudentRepository studentRepository) {
        this.scholarshipRepository = scholarshipRepository;
        this.disbursementRepository = disbursementRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Scholarship createScholarship(ScholarshipRequest request) {
        Scholarship scholarship = new Scholarship();
        scholarship.setName(request.getName());
        scholarship.setDescription(request.getDescription());
        scholarship.setAmount(request.getAmount());
        scholarship.setPercentage(request.isPercentage());
        return scholarshipRepository.save(scholarship);
    }

    public List<Scholarship> getAllScholarships() {
        return scholarshipRepository.findAll();
    }

    @Transactional
    public Disbursement disburseScholarship(Long scholarshipId, Long studentId, BigDecimal baseFee) {
        Scholarship scholarship = scholarshipRepository.findById(scholarshipId)
            .orElseThrow(() -> new ResourceNotFoundException("Scholarship not found: " + scholarshipId));
        
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));

        BigDecimal disbursementAmount;
        if (scholarship.isPercentage()) {
            disbursementAmount = baseFee.multiply(scholarship.getAmount()).divide(new BigDecimal(100));
        } else {
            disbursementAmount = scholarship.getAmount();
        }

        Disbursement disbursement = new Disbursement();
        disbursement.setStudent(student);
        disbursement.setScholarship(scholarship);
        disbursement.setDisbursementDate(LocalDate.now());
        disbursement.setAmount(disbursementAmount);

        return disbursementRepository.save(disbursement);
    }

    public List<Disbursement> getStudentDisbursements(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return disbursementRepository.findByStudentOrderByDisbursementDateDesc(student);
    }
}
