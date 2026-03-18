package com.smartschoolhub.controller;

import com.smartschoolhub.domain.Payslip;
import com.smartschoolhub.domain.SalaryComponent;
import com.smartschoolhub.service.PayrollService;
import com.smartschoolhub.service.dto.SalaryComponentRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @PostMapping("/components")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaryComponent> addComponent(@Valid @RequestBody SalaryComponentRequest request) {
        return new ResponseEntity<>(payrollService.addSalaryComponent(request), HttpStatus.CREATED);
    }

    @GetMapping("/components/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<SalaryComponent> getComponents(@PathVariable Long teacherId) {
        return payrollService.getTeacherSalaryComponents(teacherId);
    }

    @DeleteMapping("/components/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        payrollService.removeSalaryComponent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payslips")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Payslip> generatePayslip(@RequestParam Long teacherId, @RequestParam String monthYear) {
        return new ResponseEntity<>(payrollService.generatePayslip(teacherId, monthYear), HttpStatus.CREATED);
    }

    @GetMapping("/payslips/teacher/{teacherId}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<Payslip> getPayslips(@PathVariable Long teacherId) {
        return payrollService.getTeacherPayslips(teacherId);
    }
}
