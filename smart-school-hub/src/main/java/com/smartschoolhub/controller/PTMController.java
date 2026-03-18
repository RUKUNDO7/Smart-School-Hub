package com.smartschoolhub.controller;

import com.smartschoolhub.domain.MeetingSlot;
import com.smartschoolhub.domain.PTMAppointment;
import com.smartschoolhub.service.PTMService;
import com.smartschoolhub.service.dto.MeetingSlotRequest;
import com.smartschoolhub.service.dto.PTMAppointmentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ptm")
public class PTMController {
    private final PTMService ptmService;

    public PTMController(PTMService ptmService) {
        this.ptmService = ptmService;
    }

    @PostMapping("/slots")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<MeetingSlot> createSlot(@Valid @RequestBody MeetingSlotRequest request) {
        return new ResponseEntity<>(ptmService.createSlot(request), HttpStatus.CREATED);
    }

    @GetMapping("/slots/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public List<MeetingSlot> getAvailableSlots(@PathVariable Long teacherId) {
        return ptmService.getAvailableSlots(teacherId);
    }

    @PostMapping("/appointments")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public ResponseEntity<PTMAppointment> bookAppointment(@Valid @RequestBody PTMAppointmentRequest request) {
        return new ResponseEntity<>(ptmService.bookAppointment(request), HttpStatus.CREATED);
    }

    @PatchMapping("/appointments/{id}/feedback")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<Void> addFeedback(@PathVariable Long id, @RequestParam String feedback) {
        ptmService.addTeacherFeedback(id, feedback);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/appointments/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT','TEACHER')")
    public List<PTMAppointment> getStudentAppointments(@PathVariable Long studentId) {
        return ptmService.getStudentAppointments(studentId);
    }

    @GetMapping("/appointments/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<PTMAppointment> getTeacherAppointments(@PathVariable Long teacherId) {
        return ptmService.getTeacherAppointments(teacherId);
    }
}
