package com.smartschoolhub.service;

import com.smartschoolhub.domain.MeetingSlot;
import com.smartschoolhub.domain.PTMAppointment;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.repository.MeetingSlotRepository;
import com.smartschoolhub.repository.PTMAppointmentRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.MeetingSlotRequest;
import com.smartschoolhub.service.dto.PTMAppointmentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PTMService {
    private final MeetingSlotRepository slotRepository;
    private final PTMAppointmentRepository appointmentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public PTMService(MeetingSlotRepository slotRepository,
                      PTMAppointmentRepository appointmentRepository,
                      TeacherRepository teacherRepository,
                      StudentRepository studentRepository) {
        this.slotRepository = slotRepository;
        this.appointmentRepository = appointmentRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public MeetingSlot createSlot(MeetingSlotRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));

        MeetingSlot slot = new MeetingSlot();
        slot.setTeacher(teacher);
        slot.setStartTime(request.getStartTime());
        slot.setEndTime(request.getEndTime());
        
        return slotRepository.save(slot);
    }

    public List<MeetingSlot> getAvailableSlots(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));
        return slotRepository.findByTeacherAndIsBookedFalse(teacher);
    }

    @Transactional
    public PTMAppointment bookAppointment(PTMAppointmentRequest request) {
        MeetingSlot slot = slotRepository.findById(request.getSlotId())
            .orElseThrow(() -> new ResourceNotFoundException("Slot not found: " + request.getSlotId()));

        if (slot.isBooked()) {
            throw new IllegalArgumentException("Slot is already booked");
        }

        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        PTMAppointment appointment = new PTMAppointment();
        appointment.setSlot(slot);
        appointment.setStudent(student);
        appointment.setParentNote(request.getParentNote());

        slot.setBooked(true);
        slotRepository.save(slot);

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void addTeacherFeedback(Long appointmentId, String feedback) {
        PTMAppointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + appointmentId));
        appointment.setTeacherFeedback(feedback);
        appointmentRepository.save(appointment);
    }

    public List<PTMAppointment> getStudentAppointments(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return appointmentRepository.findByStudent(student);
    }

    public List<PTMAppointment> getTeacherAppointments(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));
        return appointmentRepository.findByTeacher(teacher);
    }
}
