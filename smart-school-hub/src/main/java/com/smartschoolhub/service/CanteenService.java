package com.smartschoolhub.service;

import com.smartschoolhub.domain.CanteenOrder;
import com.smartschoolhub.domain.MenuItem;
import com.smartschoolhub.domain.Student;
import com.smartschoolhub.repository.CanteenOrderRepository;
import com.smartschoolhub.repository.MenuItemRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.service.dto.CanteenOrderRequest;
import com.smartschoolhub.service.dto.MenuItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CanteenService {
    private final MenuItemRepository menuItemRepository;
    private final CanteenOrderRepository canteenOrderRepository;
    private final StudentRepository studentRepository;

    public CanteenService(MenuItemRepository menuItemRepository,
                          CanteenOrderRepository canteenOrderRepository,
                          StudentRepository studentRepository) {
        this.menuItemRepository = menuItemRepository;
        this.canteenOrderRepository = canteenOrderRepository;
        this.studentRepository = studentRepository;
    }

    public List<MenuItem> getAvailableMenuItems() {
        return menuItemRepository.findByIsAvailableTrue();
    }

    @Transactional
    public MenuItem createMenuItem(MenuItemRequest request) {
        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setIsAvailable(true);
        return menuItemRepository.save(item);
    }

    @Transactional
    public MenuItem updateMenuItemAvailability(Long id, boolean isAvailable) {
        MenuItem item = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + id));
        item.setIsAvailable(isAvailable);
        return menuItemRepository.save(item);
    }

    @Transactional
    public CanteenOrder placeOrder(CanteenOrderRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId()));

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
            .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + request.getMenuItemId()));

        if (!menuItem.getIsAvailable()) {
            throw new IllegalArgumentException("Menu item is currently not available: " + menuItem.getName());
        }

        BigDecimal totalPrice = menuItem.getPrice().multiply(new BigDecimal(request.getQuantity()));

        CanteenOrder order = new CanteenOrder();
        order.setStudent(student);
        order.setMenuItem(menuItem);
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(totalPrice);
        order.setOrderDate(LocalDateTime.now());

        return canteenOrderRepository.save(order);
    }

    public List<CanteenOrder> getStudentOrders(Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + studentId));
        return canteenOrderRepository.findByStudentOrderByOrderDateDesc(student);
    }
}
