package com.smartschoolhub.service;

import com.smartschoolhub.domain.Exam;
import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Subject;
import com.smartschoolhub.repository.ExamRepository;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.SubjectRepository;
import com.smartschoolhub.service.dto.ExamRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;

    public ExamService(ExamRepository examRepository,
                       SchoolClassRepository schoolClassRepository,
                       SubjectRepository subjectRepository) {
        this.examRepository = examRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
    }

    public List<Exam> getAll() {
        return examRepository.findAll();
    }

    public Exam getById(Long id) {
        return examRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exam not found: " + id));
    }

    public Exam create(ExamRequest request) {
        Exam exam = new Exam();
        apply(exam, request);
        return examRepository.save(exam);
    }

    public Exam update(Long id, ExamRequest request) {
        Exam exam = getById(id);
        apply(exam, request);
        return examRepository.save(exam);
    }

    public void delete(Long id) {
        Exam exam = getById(id);
        examRepository.delete(exam);
    }

    private void apply(Exam exam, ExamRequest request) {
        SchoolClass schoolClass = schoolClassRepository.findById(request.getClassId())
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getClassId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));

        exam.setSchoolClass(schoolClass);
        exam.setSubject(subject);
        exam.setDate(request.getDate());
    }
}
