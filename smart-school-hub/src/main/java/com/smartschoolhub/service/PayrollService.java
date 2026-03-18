package com.smartschoolhub.service;

import com.smartschoolhub.domain.Payslip;
import com.smartschoolhub.domain.SalaryComponent;
import com.smartschoolhub.domain.Teacher;
import com.smartschoolhub.repository.PayslipRepository;
import com.smartschoolhub.repository.SalaryComponentRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.SalaryComponentRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PayrollService {
    private final TeacherRepository teacherRepository;
    private final SalaryComponentRepository salaryComponentRepository;
    private final PayslipRepository payslipRepository;

    public PayrollService(TeacherRepository teacherRepository,
                          SalaryComponentRepository salaryComponentRepository,
                          PayslipRepository payslipRepository) {
        this.teacherRepository = teacherRepository;
        this.salaryComponentRepository = salaryComponentRepository;
        this.payslipRepository = payslipRepository;
    }

    @Transactional
    public SalaryComponent addSalaryComponent(SalaryComponentRequest request) {
        Teacher teacher = teacherRepository.findById(request.getTeacherId())
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId()));

        SalaryComponent component = new SalaryComponent();
        component.setTeacher(teacher);
        component.setName(request.getName());
        component.setAmount(request.getAmount());
        component.setDeduction(request.isDeduction());

        return salaryComponentRepository.save(component);
    }

    public List<SalaryComponent> getTeacherSalaryComponents(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));
        return salaryComponentRepository.findByTeacher(teacher);
    }

    @Transactional
    public void removeSalaryComponent(Long id) {
        salaryComponentRepository.deleteById(id);
    }

    @Transactional
    public Payslip generatePayslip(Long teacherId, String monthYear) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));

        if (payslipRepository.findByTeacherAndMonthYear(teacher, monthYear).isPresent()) {
            throw new IllegalArgumentException("Payslip already exists for " + monthYear);
        }

        List<SalaryComponent> components = salaryComponentRepository.findByTeacher(teacher);
        BigDecimal netSalary = BigDecimal.ZERO;

        for (SalaryComponent comp : components) {
            if (comp.isDeduction()) {
                netSalary = netSalary.subtract(comp.getAmount());
            } else {
                netSalary = netSalary.add(comp.getAmount());
            }
        }

        Payslip payslip = new Payslip();
        payslip.setTeacher(teacher);
        payslip.setMonthYear(monthYear);
        payslip.setPaymentDate(LocalDate.now());
        payslip.setNetSalary(netSalary);
        payslip.setPaid(true); // Assuming auto-pay for now

        return payslipRepository.save(payslip);
    }

    public List<Payslip> getTeacherPayslips(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + teacherId));
        return payslipRepository.findByTeacherOrderByPaymentDateDesc(teacher);
    }
}
