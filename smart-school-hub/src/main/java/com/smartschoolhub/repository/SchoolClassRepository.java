package com.smartschoolhub.repository;

import com.smartschoolhub.domain.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findByTeacherId(Long teacherId);
}
