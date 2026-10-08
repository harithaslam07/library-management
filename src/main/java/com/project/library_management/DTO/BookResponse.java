package com.project.library_management.DTO;

import com.project.library_management.model.Book;
import lombok.*;

@Getter
@AllArgsConstructor
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String category;
    private int availableCopies;

    public static BookResponse from(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableCopies()
        );
    }
}