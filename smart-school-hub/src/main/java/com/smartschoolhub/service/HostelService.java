package com.smartschoolhub.service;

import com.smartschoolhub.domain.BedAllocation;
import com.smartschoolhub.domain.Hostel;
import com.smartschoolhub.domain.Room;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.BedAllocationRepository;
import com.smartschoolhub.repository.HostelRepository;
import com.smartschoolhub.repository.RoomRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.BedAllocationRequest;
import com.smartschoolhub.service.dto.HostelRequest;
import com.smartschoolhub.service.dto.RoomRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HostelService {
    private final HostelRepository hostelRepository;
    private final RoomRepository roomRepository;
    private final BedAllocationRepository bedAllocationRepository;
    private final StudentRepository studentRepository;

    public HostelService(HostelRepository hostelRepository,
                         RoomRepository roomRepository,
                         BedAllocationRepository bedAllocationRepository,
                         StudentRepository studentRepository) {
        this.hostelRepository = hostelRepository;
        this.roomRepository = roomRepository;
        this.bedAllocationRepository = bedAllocationRepository;
        this.studentRepository = studentRepository;
    }

    public List<Hostel> getAllHostels() {
        return hostelRepository.findAll();
    }

    public Hostel createHostel(HostelRequest request) {
        Hostel hostel = new Hostel();
        hostel.setName(request.getName());
        hostel.setType(request.getType());
        hostel.setAddress(request.getAddress());
        return hostelRepository.save(hostel);
    }

    public List<Room> getRoomsByHostel(Long hostelId) {
        Hostel hostel = hostelRepository.findById(hostelId)
            .orElseThrow(() -> new ResourceNotFoundException("Hostel not found: " + hostelId));
        return roomRepository.findByHostel(hostel);
    }

    public Room createRoom(RoomRequest request) {
        Hostel hostel = hostelRepository.findById(request.getHostelId())
            .orElseThrow(() -> new ResourceNotFoundException("Hostel not found: " + request.getHostelId()));

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setHostel(hostel);
        room.setCapacity(request.getCapacity());
        return roomRepository.save(room);
    }

    @Transactional
    public BedAllocation allocateBed(BedAllocationRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
            .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + request.getRoomId()));

        if (room.getOccupancyCount() >= room.getCapacity()) {
            throw new IllegalArgumentException("Room is at full capacity");
        }

        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        // Check if student already has an active allocation
        bedAllocationRepository.findByStudentAndReleaseDateIsNull(student).ifPresent(a -> {
            throw new IllegalArgumentException("Student already has an active bed allocation");
        });

        BedAllocation allocation = new BedAllocation();
        allocation.setRoom(room);
        allocation.setStudent(student);
        allocation.setAllocationDate(request.getAllocationDate());

        room.setOccupancyCount(room.getOccupancyCount() + 1);
        roomRepository.save(room);

        return bedAllocationRepository.save(allocation);
    }

    @Transactional
    public void releaseBed(Long allocationId) {
        BedAllocation allocation = bedAllocationRepository.findById(allocationId)
            .orElseThrow(() -> new ResourceNotFoundException("Allocation not found: " + allocationId));

        if (allocation.getReleaseDate() != null) {
            throw new IllegalArgumentException("Bed already released");
        }

        allocation.setReleaseDate(java.time.LocalDate.now());
        bedAllocationRepository.save(allocation);

        Room room = allocation.getRoom();
        room.setOccupancyCount(room.getOccupancyCount() - 1);
        roomRepository.save(room);
    }
}
