package com.smartschoolhub.controller;

import com.smartschoolhub.domain.RouteAssignment;
import com.smartschoolhub.domain.TransportRoute;
import com.smartschoolhub.domain.Vehicle;
import com.smartschoolhub.service.TransportService;
import com.smartschoolhub.service.dto.RouteAssignmentRequest;
import com.smartschoolhub.service.dto.TransportRouteRequest;
import com.smartschoolhub.service.dto.VehicleRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
public class TransportController {
    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("/routes")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT','PARENT')")
    public List<TransportRoute> getAllRoutes() {
        return transportService.getAllRoutes();
    }

    @PostMapping("/routes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransportRoute> createRoute(@Valid @RequestBody TransportRouteRequest request) {
        return new ResponseEntity<>(transportService.createRoute(request), HttpStatus.CREATED);
    }

    @GetMapping("/vehicles")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<Vehicle> getAllVehicles() {
        return transportService.getAllVehicles();
    }

    @PostMapping("/vehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody VehicleRequest request) {
        return new ResponseEntity<>(transportService.createVehicle(request), HttpStatus.CREATED);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteAssignment> assignRoute(@Valid @RequestBody RouteAssignmentRequest request) {
        return new ResponseEntity<>(transportService.assignRoute(request), HttpStatus.CREATED);
    }

    @PostMapping("/remove-assignment/{assignmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeAssignment(@PathVariable Long assignmentId) {
        transportService.removeAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}
