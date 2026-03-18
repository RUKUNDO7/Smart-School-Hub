package com.smartschoolhub.service;

import com.smartschoolhub.domain.SchoolClass;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.SchoolClassRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.StudentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;

    public StudentService(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        this.studentRepository = studentRepository;
        this.schoolClassRepository = schoolClassRepository;
    }

    public Page<Student> getAll(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + id));
    }

    public Student create(StudentRequest request) {
        Student student = new Student();
        apply(student, request);
        return studentRepository.save(student);
    }

    public Student update(Long id, StudentRequest request) {
        Student student = getById(id);
        apply(student, request);
        return studentRepository.save(student);
    }

    public void delete(Long id) {
        Student student = getById(id);
        studentRepository.delete(student);
    }

    private void apply(Student student, StudentRequest request) {
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setDob(request.getDob());
        student.setGender(request.getGender());
        student.setEmail(request.getEmail());
        student.setPhone(request.getPhone());
        student.setAddress(request.getAddress());

        if (request.getClassId() != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(request.getClassId())
                .orElseThrow(() -> new ResourceNotFoundException("Class not found: " + request.getClassId()));
            student.setSchoolClass(schoolClass);
        } else {
            student.setSchoolClass(null);
        }
    }
}
