package com.project.library_management.DTO;

import com.project.library_management.model.Issue;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class IssueResponse {

    private Long issueId;
    private String bookTitle;
    private String memberName;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fine;

    public static IssueResponse from(Issue issue) {
        return new IssueResponse(
                issue.getId(),
                issue.getBook().getTitle(),
                issue.getMember().getName(),
                issue.getIssueDate(),
                issue.getDueDate(),
                issue.getReturnDate(),
                issue.getFine()
        );
    }
}