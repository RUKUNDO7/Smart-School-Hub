package com.smartschoolhub.service;

import com.smartschoolhub.domain.Book;
import com.smartschoolhub.domain.LibraryLoan;
import com.smartschoolhub.domain.LoanStatus;
import com.smartschoolhub.repository.BookRepository;
import com.smartschoolhub.repository.LibraryLoanRepository;
import com.smartschoolhub.repository.StudentRepository;
import com.smartschoolhub.repository.TeacherRepository;
import com.smartschoolhub.service.dto.LibraryLoanRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LibraryLoanService {
    private final LibraryLoanRepository libraryLoanRepository;
    private final BookRepository bookRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public LibraryLoanService(LibraryLoanRepository libraryLoanRepository,
                              BookRepository bookRepository,
                              StudentRepository studentRepository,
                              TeacherRepository teacherRepository) {
        this.libraryLoanRepository = libraryLoanRepository;
        this.bookRepository = bookRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public List<LibraryLoan> getAll() {
        return libraryLoanRepository.findAll();
    }

    public LibraryLoan getById(Long id) {
        return libraryLoanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found: " + id));
    }

    @Transactional
    public LibraryLoan issueBook(LibraryLoanRequest request) {
        Book book = bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + request.getBookId()));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available for loan");
        }

        LibraryLoan loan = new LibraryLoan();
        loan.setBook(book);
        loan.setLoanDate(request.getLoanDate());
        loan.setDueDate(request.getDueDate());
        loan.setStatus(LoanStatus.ISSUED);

        if (request.getStudentId() != null) {
            loan.setStudent(studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found: " + request.getStudentId())));
        } else if (request.getTeacherId() != null) {
            loan.setTeacher(teacherRepository.findById(request.getTeacherId())
                .orElseThrow(() -> new ResourceNotFoundException("Teacher not found: " + request.getTeacherId())));
        } else {
            throw new RuntimeException("Either Student ID or Teacher ID must be provided");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        return libraryLoanRepository.save(loan);
    }

    @Transactional
    public LibraryLoan returnBook(Long id) {
        LibraryLoan loan = getById(id);
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("Book already returned");
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return libraryLoanRepository.save(loan);
    }

    public void delete(Long id) {
        LibraryLoan loan = getById(id);
        libraryLoanRepository.delete(loan);
    }
}
