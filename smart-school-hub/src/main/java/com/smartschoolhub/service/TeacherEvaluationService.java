package com.smartschoolhub.service;

import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.domain.TeacherEvaluation;
import com.smartschoolhub.repository.TeacherEvaluationRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.TeacherEvaluationRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TeacherEvaluationService {
    private final TeacherEvaluationRepository teacherEvaluationRepository;
    private final TeacherRepository teacherRepository;

    public TeacherEvaluationService(TeacherEvaluationRepository teacherEvaluationRepository, TeacherRepository teacherRepository) {
        this.teacherEvaluationRepository = teacherEvaluationRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<TeacherEvaluation> getAll() {
        return teacherEvaluationRepository.findAll();
    }

    public TeacherEvaluation getById(Long id) {
        return teacherEvaluationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher evaluation not found: " + id));
    }

    public List<TeacherEvaluation> getByTeacher(Long teacherId) {
        return teacherEvaluationRepository.findByTeacherId(teacherId);
    }

    public TeacherEvaluation create(TeacherEvaluationRequest request) {
        TeacherEvaluation evaluation = new TeacherEvaluation();
        apply(evaluation, request);
        return teacherEvaluationRepository.save(evaluation);
    }

    public TeacherEvaluation update(Long id, TeacherEvaluationRequest request) {
        TeacherEvaluation evaluation = getById(id);
        apply(evaluation, request);
        return teacherEvaluationRepository.save(evaluation);
    }

    public void delete(Long id) {
        TeacherEvaluation evaluation = getById(id);
        teacherEvaluationRepository.delete(evaluation);
    }

    private void apply(TeacherEvaluation evaluation, TeacherEvaluationRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));
        evaluation.setTeacher(teacher);
        evaluation.setDate(request.getDate());
        evaluation.setScore(request.getScore());
        evaluation.setNotes(request.getNotes());
    }
}
