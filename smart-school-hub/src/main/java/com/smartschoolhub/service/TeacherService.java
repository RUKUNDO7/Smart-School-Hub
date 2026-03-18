package com.smartschoolhub.service;

import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Subject;
import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.SubjectRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.TeacherRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;

    public TeacherService(TeacherRepository teacherRepository,
                          SubjectRepository subjectRepository,
                          SchoolClassRepository schoolClassRepository) {
        this.teacherRepository = teacherRepository;
        this.subjectRepository = subjectRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

    public Page<Teacher> getAll(Pageable pageable) {
        return teacherRepository.findAll(pageable);
    }

    public Teacher getById(Long id) {
        return teacherRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + id));
    }

    public Teacher create(TeacherRequest request) {
        Teacher teacher = new Teacher();
        applyFields(teacher, request);
        teacher = teacherRepository.save(teacher);
        updateAssignments(teacher, request);
        return teacherRepository.save(teacher);
    }

    public Teacher update(Long id, TeacherRequest request) {
        Teacher teacher = getById(id);
        applyFields(teacher, request);
        teacher = teacherRepository.save(teacher);
        updateAssignments(teacher, request);
        return teacherRepository.save(teacher);
    }

    public void delete(Long id) {
        Teacher teacher = getById(id);
        clearAssignments(teacher);
        teacherRepository.delete(teacher);
    }

    private void applyFields(Teacher teacher, TeacherRequest request) {
        teacher.setFirstName(request.getFirstName());
        teacher.setLastName(request.getLastName());
        teacher.setEmail(request.getEmail());
        teacher.setPhone(request.getPhone());
    }

    private void updateAssignments(Teacher teacher, TeacherRequest request) {
        if (request.getSubjectIds() != null) {
            Set<Long> targetIds = new HashSet<>(request.getSubjectIds());
            List<Subject> current = subjectRepository.findByTeacherId(teacher.getId());
            for (Subject subject : current) {
                if (!targetIds.contains(subject.getId())) {
                    subject.setTeacher(null);
                    subjectRepository.save(subject);
                }
            }
            for (Long subjectId : targetIds) {
                Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + subjectId));
                subject.setTeacher(teacher);
                subjectRepository.save(subject);
            }
        }

        if (request.getClassIds() != null) {
            Set<Long> targetIds = new HashSet<>(request.getClassIds());
            List<SchoolClass> current = schoolClassRepository.findByTeacherId(teacher.getId());
            for (SchoolClass schoolClass : current) {
                if (!targetIds.contains(schoolClass.getId())) {
                    schoolClass.setTeacher(null);
                    schoolClassRepository.save(schoolClass);
                }
            }
            for (Long classId : targetIds) {
                SchoolClass schoolClass = schoolClassRepository.findById(classId)
                    .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + classId));
                schoolClass.setTeacher(teacher);
                schoolClassRepository.save(schoolClass);
            }
        }
    }

    private void clearAssignments(Teacher teacher) {
        List<Subject> subjects = subjectRepository.findByTeacherId(teacher.getId());
        for (Subject subject : subjects) {
            subject.setTeacher(null);
            subjectRepository.save(subject);
        }

        List<SchoolClass> classes = schoolClassRepository.findByTeacherId(teacher.getId());
        for (SchoolClass schoolClass : classes) {
            schoolClass.setTeacher(null);
            schoolClassRepository.save(schoolClass);
        }
    }
}
