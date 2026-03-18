package com.smartschoolhub.service;

import com.smartschoolhub.domain.RouteAssignment;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.domain.TransportRoute;
import com.smartschoolhub.domain.Vehicle;
import com.smartschoolhub.repository.RouteAssignmentRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.repository.TransportRouteRepository;
import com.smartschoolhub.repository.VehicleRepository;
import com.smartschoolhub.service.dto.RouteAssignmentRequest;
import com.smartschoolhub.service.dto.TransportRouteRequest;
import com.smartschoolhub.service.dto.VehicleRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransportService {
    private final TransportRouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;
    private final RouteAssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    public TransportService(TransportRouteRepository routeRepository,
                            VehicleRepository vehicleRepository,
                            RouteAssignmentRepository assignmentRepository,
                            StudentRepository studentRepository) {
        this.routeRepository = routeRepository;
        this.vehicleRepository = vehicleRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
    }

    public List<TransportRoute> getAllRoutes() {
        return routeRepository.findAll();
    }

    public TransportRoute createRoute(TransportRouteRequest request) {
        TransportRoute route = new TransportRoute();
        route.setRouteName(request.getRouteName());
        route.setRouteDescription(request.getRouteDescription());
        route.setRouteCost(request.getRouteCost());
        return routeRepository.save(route);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle createVehicle(VehicleRequest request) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleNumber(request.getVehicleNumber());
        vehicle.setVehicleModel(request.getVehicleModel());
        vehicle.setCapacity(request.getCapacity());
        vehicle.setDriverName(request.getDriverName());
        vehicle.setDriverPhone(request.getDriverPhone());
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public RouteAssignment assignRoute(RouteAssignmentRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        TransportRoute route = routeRepository.findById(request.getRouteId())
            .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + request.getRouteId()));

        Vehicle vehicle = null;
        if (request.getVehicleId() != null) {
            vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVehicleId()));
        }

        // Check for existing active assignment
        assignmentRepository.findByStudentAndEndDateIsNull(student).ifPresent(a -> {
            throw new IllegalArgumentException("Student already has an active transport assignment");
        });

        RouteAssignment assignment = new RouteAssignment();
        assignment.setStudent(student);
        assignment.setRoute(route);
        assignment.setVehicle(vehicle);
        assignment.setStartDate(request.getStartDate());

        return assignmentRepository.save(assignment);
    }

    @Transactional
    public void removeAssignment(Long assignmentId) {
        RouteAssignment assignment = assignmentRepository.findById(assignmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment not found: " + assignmentId));

        if (assignment.getEndDate() != null) {
            throw new IllegalArgumentException("Assignment already closed");
        }

        assignment.setEndDate(java.time.LocalDate.now());
        assignmentRepository.save(assignment);
    }
}
