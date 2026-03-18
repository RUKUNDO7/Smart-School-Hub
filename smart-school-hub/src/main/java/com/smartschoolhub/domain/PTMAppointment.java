package com.smartschoolhub.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "ptm_appointments")
public class PTMAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    private MeetingSlot slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(columnDefinition = "TEXT")
    private String parentNote;

    @Column(columnDefinition = "TEXT")
    private String teacherFeedback;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeetingSlot getSlot() {
        return slot;
    }

    public void setSlot(MeetingSlot slot) {
        this.slot = slot;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getParentNote() {
        return parentNote;
    }

    public void setParentNote(String parentNote) {
        this.parentNote = parentNote;
    }

    public String getTeacherFeedback() {
        return teacherFeedback;
    }

    public void setTeacherFeedback(String teacherFeedback) {
        this.teacherFeedback = teacherFeedback;
    }
}
