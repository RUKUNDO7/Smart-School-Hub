package com.smartschoolhub.service;

import com.smartschoolhub.domain.*;
import com.smartschoolhub.repository.*;
import com.smartschoolhub.service.dto.AssignmentRequest;
import com.smartschoolhub.service.dto.SubmissionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             SubmissionRepository submissionRepository,
                             SubjectRepository subjectRepository,
                             TeacherRepository teacherRepository,
                             StudentRepository studentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public Assignment createAssignment(AssignmentRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
            .orElseThrow(() -> new ResourceNotFoundException("Subject not found: " + request.getSubjectId()));

        Teacher teacher = teacherRepository.findById(request.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));

        Assignment assignment = new Assignment();
        assignment.setTitle(request.getTitle());
        assignment.setDescription(request.getDescription());
        assignment.setSubject(subject);
        assignment.setTeacher(teacher);
        assignment.setDueDate(request.getDueDate());

        return assignmentRepository.save(assignment);
    }

    public Submission createSubmission(SubmissionRequest request) {
        Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
            .orElseThrow(() -> new ResourceNotFoundException("Assignment not found: " + request.getAssignmentId()));

        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        // Check if already submitted
        submissionRepository.findByAssignmentAndStudent(assignment, student).ifPresent(s -> {
            throw new IllegalArgumentException("Assignment already submitted by this student");
        });

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setRemarks(request.getRemarks());

        if (submission.getSubmissionDate().isAfter(assignment.getDueDate())) {
            submission.setStatus("LATE");
        } else {
            submission.setStatus("SUBMITTED");
        }

        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment not found: " + assignmentId));
        return submissionRepository.findByAssignment(assignment);
    }
}
