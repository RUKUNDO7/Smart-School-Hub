package com.smartschoolhub.service;

import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Subject;
import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.SubjectRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.SubjectRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          SchoolClassRepository schoolClassRepository,
                          TeacherRepository teacherRepository) {
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }

    public Subject getById(Long id) {
        return subjectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + id));
    }

    public Subject create(SubjectRequest request) {
        Subject subject = new Subject();
        apply(subject, request);
        return subjectRepository.save(subject);
    }

    public Subject update(Long id, SubjectRequest request) {
        Subject subject = getById(id);
        apply(subject, request);
        return subjectRepository.save(subject);
    }

    public void delete(Long id) {
        Subject subject = getById(id);
        subjectRepository.delete(subject);
    }

    private void apply(Subject subject, SubjectRequest request) {
        subject.setName(request.getName());

        SchoolClass schoolClass = schoolClassRepository.findById(request.getClassId())
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getClassId()));
        subject.setSchoolClass(schoolClass);

        if (request.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));
            subject.setTeacher(teacher);
        } else {
            subject.setTeacher(null);
        }
    }
}
