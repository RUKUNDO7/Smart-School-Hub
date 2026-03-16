package com.smartschoolhub.repository;

import com.smartschoolhub.domain.TeacherAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TeacherAttendanceRepository extends JpaRepository<TeacherAttendance, Long> {
    List<TeacherAttendance> findByTeacherId(Long teacherId);
    List<TeacherAttendance> findByDate(LocalDate date);
}
