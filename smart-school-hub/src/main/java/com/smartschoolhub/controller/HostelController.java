package com.smartschoolhub.controller;

import com.smartschoolhub.domain.BedAllocation;
import com.smartschoolhub.domain.Hostel;
import com.smartschoolhub.domain.Room;
import com.smartschoolhub.service.HostelService;
import com.smartschoolhub.service.dto.BedAllocationRequest;
import com.smartschoolhub.service.dto.HostelRequest;
import com.smartschoolhub.service.dto.RoomRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hostels")
public class HostelController {
    private final HostelService hostelService;

    public HostelController(HostelService hostelService) {
        this.hostelService = hostelService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<Hostel> getAllHostels() {
        return hostelService.getAllHostels();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hostel> createHostel(@Valid @RequestBody HostelRequest request) {
        return new ResponseEntity<>(hostelService.createHostel(request), HttpStatus.CREATED);
    }

    @GetMapping("/{hostelId}/rooms")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<Room> getRooms(@PathVariable Long hostelId) {
        return hostelService.getRoomsByHostel(hostelId);
    }

    @PostMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody RoomRequest request) {
        return new ResponseEntity<>(hostelService.createRoom(request), HttpStatus.CREATED);
    }

    @PostMapping("/allocate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BedAllocation> allocateBed(@Valid @RequestBody BedAllocationRequest request) {
        return new ResponseEntity<>(hostelService.allocateBed(request), HttpStatus.CREATED);
    }

    @PostMapping("/release/{allocationId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> releaseBed(@PathVariable Long allocationId) {
        hostelService.releaseBed(allocationId);
        return ResponseEntity.noContent().build();
    }
}
