package com.project.library_management.service;
import com.project.library_management.DTO.IssueResponse;
import com.project.library_management.model.Book;
import com.project.library_management.model.Issue;
import com.project.library_management.model.Member;
import com.project.library_management.repository.BookRepository;
import com.project.library_management.repository.IssueRepository;
import com.project.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class IssueService {

    private static final int LOAN_DAYS = 14;

    private final IssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IssueResponse issueBook(Long bookId, Long memberId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found: " + memberId));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("No copies available for: " + book.getTitle());
        }

        if (issueRepository.existsByBookIdAndMemberIdAndReturnDateIsNull(bookId, memberId)) {
            throw new RuntimeException("Member already has this book");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Issue issue = new Issue();
        issue.setBook(book);
        issue.setMember(member);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(LOAN_DAYS));
        issue.setFine(0);
        Issue saved = issueRepository.save(issue);                         // CHANGED: keep the saved entity
        return IssueResponse.from(saved);

    }
}
