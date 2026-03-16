package com.smartschoolhub.service;

import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Subject;
import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.domain.TimetableEntry;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.SubjectRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.repository.TimetableEntryRepository;
import com.smartschoolhub.service.dto.TimetableEntryRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {
    private final TimetableEntryRepository timetableEntryRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public TimetableService(TimetableEntryRepository timetableEntryRepository,
                            SchoolClassRepository schoolClassRepository,
                            SubjectRepository subjectRepository,
                            TeacherRepository teacherRepository) {
        this.timetableEntryRepository = timetableEntryRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<TimetableEntry> getAll() {
        return timetableEntryRepository.findAll();
    }

    public TimetableEntry getById(Long id) {
        return timetableEntryRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found: " + id));
    }

    public List<TimetableEntry> getByClass(Long classId) {
        return timetableEntryRepository.findBySchoolClassId(classId);
    }

    public List<TimetableEntry> getByTeacher(Long teacherId) {
        return timetableEntryRepository.findByTeacherId(teacherId);
    }

    public TimetableEntry create(TimetableEntryRequest request) {
        TimetableEntry entry = new TimetableEntry();
        apply(entry, request);
        return timetableEntryRepository.save(entry);
    }

    public TimetableEntry update(Long id, TimetableEntryRequest request) {
        TimetableEntry entry = getById(id);
        apply(entry, request);
        return timetableEntryRepository.save(entry);
    }

    public void delete(Long id) {
        TimetableEntry entry = getById(id);
        timetableEntryRepository.delete(entry);
    }

    private void apply(TimetableEntry entry, TimetableEntryRequest request) {
        SchoolClass schoolClass = schoolClassRepository.findById(request.getClassId())
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getClassId()));
        Subject subject = subjectRepository.findById(request.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));

        entry.setSchoolClass(schoolClass);
        entry.setSubject(subject);
        entry.setTeacher(teacher);
        entry.setDayOfWeek(request.getDayOfWeek());
        entry.setStartTime(request.getStartTime());
        entry.setEndTime(request.getEndTime());
    }
}
