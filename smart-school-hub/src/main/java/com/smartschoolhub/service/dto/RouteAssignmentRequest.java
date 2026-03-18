package com.smartschoolhub.service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class RouteAssignmentRequest {
    @NotNull
    private Long studentId;

    @NotNull
    private Long routeId;

    private Long vehicleId;

    @NotNull
    private LocalDate startDate;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
