package com.smartschoolhub.controller;

import com.smartschoolhub.domain.CanteenOrder;
import com.smartschoolhub.domain.MenuItem;
import com.smartschoolhub.service.CanteenService;
import com.smartschoolhub.service.dto.CanteenOrderRequest;
import com.smartschoolhub.service.dto.MenuItemRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canteen")
public class CanteenController {
    private final CanteenService canteenService;

    public CanteenController(CanteenService canteenService) {
        this.canteenService = canteenService;
    }

    @GetMapping("/menu")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<MenuItem> getMenu() {
        return canteenService.getAvailableMenuItems();
    }

    @PostMapping("/menu")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItemRequest request) {
        return new ResponseEntity<>(canteenService.createMenuItem(request), HttpStatus.CREATED);
    }

    @PatchMapping("/menu/{id}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItem> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return ResponseEntity.ok(canteenService.updateMenuItemAvailability(id, available));
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ResponseEntity<CanteenOrder> placeOrder(@Valid @RequestBody CanteenOrderRequest request) {
        return new ResponseEntity<>(canteenService.placeOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/orders/student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','STUDENT')")
    public List<CanteenOrder> getStudentOrders(@PathVariable Long studentId) {
        return canteenService.getStudentOrders(studentId);
    }
}
