package com.smartschoolhub.repository;

import com.smartschoolhub.domain.TeacherEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherEvaluationRepository extends JpaRepository<TeacherEvaluation, Long> {
    List<TeacherEvaluation> findByTeacherId(Long teacherId);
}
