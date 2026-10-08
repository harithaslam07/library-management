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
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import com.project.library_management.exception.ConflictException;
import com.project.library_management.exception.ResourceNotFoundException;
@Service
@RequiredArgsConstructor
public class IssueService {

    private static final int LOAN_DAYS = 14;
    private static final double FINE_PER_DAY = 5.0;

    private final IssueRepository issueRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public IssueResponse issueBook(Long bookId, Long memberId) {          // CHANGED: return type

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + memberId));

        if (book.getAvailableCopies() <= 0) {
            throw new ConflictException("No copies available for: " + book.getTitle());
        }

        if (issueRepository.existsByBookIdAndMemberIdAndReturnDateIsNull(bookId, memberId)) {
            throw new ConflictException("Member already has this book");
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
        return IssueResponse.from(saved);                                  // CHANGED: return the receipt
    }
    @Transactional
    public IssueResponse returnBook(Long issueId) {

        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found: " + issueId));

        if (issue.getReturnDate() != null) {
            throw new ConflictException("Book already returned");
        }

        LocalDate today = LocalDate.now();
        issue.setReturnDate(today);

        long daysLate = ChronoUnit.DAYS.between(issue.getDueDate(), today);
        if (daysLate > 0) {
            issue.setFine(daysLate * FINE_PER_DAY);
        }

        Book book = issue.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        bookRepository.save(book);
        Issue saved = issueRepository.save(issue);

        return IssueResponse.from(saved);
    }
}
