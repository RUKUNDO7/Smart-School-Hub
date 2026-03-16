package com.smartschoolhub.repository;

import com.smartschoolhub.domain.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findBySchoolClassId(Long classId);
    List<TimetableEntry> findByTeacherId(Long teacherId);
}
