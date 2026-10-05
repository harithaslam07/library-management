package com.project.library_management.service;

import com.project.library_management.model.Book;
import com.project.library_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public Book updateBook(Long id, Book updated) {
        Book book = getBook(id);
        book.setTitle(updated.getTitle());
        book.setAuthor(updated.getAuthor());
        book.setCategory(updated.getCategory());
        book.setAvailableCopies(updated.getAvailableCopies());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}