package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public class TeacherRequest {
    @NotBlank(message = "First name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @jakarta.validation.constraints.Size(min = 2, max = 50)
    private String lastName;

    @NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Invalid email format")
    private String email;

    @jakarta.validation.constraints.Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    private Set<Long> subjectIds;

    private Set<Long> classIds;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(Set<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }

    public Set<Long> getClassIds() {
        return classIds;
    }

    public void setClassIds(Set<Long> classIds) {
        this.classIds = classIds;
    }
}
