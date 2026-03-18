package com.smartschoolhub.service;

import com.smartschoolhub.domain.Exam;
import com.smartschoolhub.domain.Grade;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.ExamRepository;
import com.smartschoolhub.repository.GradeRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.GradeRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@Transactional
public class GradeService {
    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, ExamRepository examRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
    }

    public Page<Grade> getAll(Pageable pageable) {
        return gradeRepository.findAll(pageable);
    }

    public Grade getById(Long id) {
        return gradeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Grade not found: " + id));
    }

    public List<Grade> getByStudent(Long studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public Grade create(GradeRequest request) {
        Grade grade = new Grade();
        apply(grade, request);
        return gradeRepository.save(grade);
    }

    public Grade update(Long id, GradeRequest request) {
        Grade grade = getById(id);
        apply(grade, request);
        return gradeRepository.save(grade);
    }

    public void delete(Long id) {
        Grade grade = getById(id);
        gradeRepository.delete(grade);
    }

    private void apply(Grade grade, GradeRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));
        Exam exam = examRepository.findById(request.getExamId())
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found: " + request.getExamId()));
        grade.setStudent(student);
        grade.setExam(exam);
        grade.setMarks(request.getMarks());
    }
}
