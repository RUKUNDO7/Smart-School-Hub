package com.smartschoolhub.service;

import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.SchoolClassRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SchoolClassService {
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;

    public SchoolClassService(SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository) {
        this.schoolClassRepository = schoolClassRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<SchoolClass> getAll() {
        return schoolClassRepository.findAll();
    }

    public SchoolClass getById(Long id) {
        return schoolClassRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + id));
    }

    public SchoolClass create(SchoolClassRequest request) {
        SchoolClass schoolClass = new SchoolClass();
        apply(schoolClass, request);
        return schoolClassRepository.save(schoolClass);
    }

    public SchoolClass update(Long id, SchoolClassRequest request) {
        SchoolClass schoolClass = getById(id);
        apply(schoolClass, request);
        return schoolClassRepository.save(schoolClass);
    }

    public void delete(Long id) {
        SchoolClass schoolClass = getById(id);
        schoolClassRepository.delete(schoolClass);
    }

    private void apply(SchoolClass schoolClass, SchoolClassRequest request) {
        schoolClass.setName(request.getName());
        schoolClass.setGradeLevel(request.getGradeLevel());

        if (request.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));
            schoolClass.setTeacher(teacher);
        } else {
            schoolClass.setTeacher(null);
        }
    }
}
