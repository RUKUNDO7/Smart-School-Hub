package com.smartschoolhub.repository;

import com.smartschoolhub.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
