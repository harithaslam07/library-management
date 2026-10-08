package com.project.library_management.controller;

import com.project.library_management.DTO.BookRequest;
import com.project.library_management.DTO.BookResponse;
import com.project.library_management.DTO.PageResponse;
import com.project.library_management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse add(@Valid @RequestBody BookRequest request) {
        return bookService.addBook(request);
    }

    @GetMapping
    public PageResponse<BookResponse> getAll(@RequestParam(required = false) String category,
                                             @RequestParam(required = false) String author,
                                             @RequestParam(required = false) String title,
                                             Pageable pageable) {
        return bookService.searchBooks(category, author, title, pageable);
    }

    @GetMapping("/{id}")
    public BookResponse getOne(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PutMapping("/{id}")
    public BookResponse update(@PathVariable Long id, @Valid @RequestBody BookRequest request) {
        return bookService.updateBook(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}