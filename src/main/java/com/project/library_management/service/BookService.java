package com.project.library_management.service;

import com.project.library_management.DTO.BookRequest;
import com.project.library_management.DTO.BookResponse;
import com.project.library_management.DTO.PageResponse;
import com.project.library_management.exception.ResourceNotFoundException;
import com.project.library_management.model.Book;
import com.project.library_management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public BookResponse addBook(BookRequest request) {
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setAvailableCopies(request.getAvailableCopies());
        return BookResponse.from(bookRepository.save(book));
    }

    public PageResponse<BookResponse> searchBooks(String category, String author, String title, Pageable pageable) {
        Page<Book> page;

        if (category != null && !category.isBlank()) {
            page = bookRepository.findByCategoryIgnoreCase(category, pageable);
        } else if (author != null && !author.isBlank()) {
            page = bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
        } else if (title != null && !title.isBlank()) {
            page = bookRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else {
            page = bookRepository.findAll(pageable);
        }

        return PageResponse.from(page.map(BookResponse::from));
    }

    public BookResponse getBook(Long id) {
        return BookResponse.from(findBook(id));
    }

    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = findBook(id);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setAvailableCopies(request.getAvailableCopies());
        return BookResponse.from(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }
}