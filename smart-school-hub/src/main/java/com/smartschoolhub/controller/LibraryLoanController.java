package com.smartschoolhub.controller;

import com.smartschoolhub.domain.LibraryLoan;
import com.smartschoolhub.service.LibraryLoanService;
import com.smartschoolhub.service.dto.LibraryLoanRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/library-loans")
public class LibraryLoanController {
    private final LibraryLoanService libraryLoanService;

    public LibraryLoanController(LibraryLoanService libraryLoanService) {
        this.libraryLoanService = libraryLoanService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<LibraryLoan> getAll() {
        return libraryLoanService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public LibraryLoan getById(@PathVariable Long id) {
        return libraryLoanService.getById(id);
    }

    @PostMapping("/issue")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseEntity<LibraryLoan> issueBook(@Valid @RequestBody LibraryLoanRequest request) {
        return new ResponseEntity<>(libraryLoanService.issueBook(request), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public LibraryLoan returnBook(@PathVariable Long id) {
        return libraryLoanService.returnBook(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libraryLoanService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
