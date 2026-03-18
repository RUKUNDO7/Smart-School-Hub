package com.smartschoolhub.service;

import com.smartschoolhub.domain.Book;
import com.smartschoolhub.repository.BookRepository;
import com.smartschoolhub.service.dto.BookRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    public Book create(BookRequest request) {
        Book book = new Book();
        apply(book, request);
        book.setAvailableCopies(request.getTotalCopies());
        return bookRepository.save(book);
    }

    public Book update(Long id, BookRequest request) {
        Book book = getById(id);
        int diff = request.getTotalCopies() - book.getTotalCopies();
        apply(book, request);
        book.setAvailableCopies(book.getAvailableCopies() + diff);
        if (book.getAvailableCopies() < 0) {
            throw new RuntimeException("Cannot reduce total copies below currently issued copies");
        }
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
    }

    private void apply(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setCategory(request.getCategory());
        book.setTotalCopies(request.getTotalCopies());
    }
}
