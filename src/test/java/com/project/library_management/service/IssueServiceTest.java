package com.project.library_management.service;

import com.project.library_management.exception.ConflictException;
import com.project.library_management.model.Book;
import com.project.library_management.model.Issue;
import com.project.library_management.model.Member;
import com.project.library_management.repository.BookRepository;
import com.project.library_management.repository.IssueRepository;
import com.project.library_management.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.library_management.DTO.IssueResponse;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private IssueService issueService;

    @Test
    void issueBook_throwsConflict_whenNoCopiesLeft() {

        // ARRANGE: prepare the situation
        Book book = new Book(1L, "1984", "George Orwell", "Political", 0);
        Member member = new Member(1L, "Aslam", "aslam@example.com");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // ACT and ASSERT: call the method, and check that it throws
        assertThatThrownBy(() -> issueService.issueBook(1L, 1L))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("No copies available");

        // ASSERT: nothing was saved
        verify(issueRepository, never()).save(any(Issue.class));

    }
    @Test
    void issueBook_success_reducesCopiesAndReturnsReceipt() {

        // 1. Pretend: book 1 has 1 copy, member 1 exists, and he doesn't hold the book
        Book book = new Book(1L, "1984", "George Orwell", "Political", 1);
        Member member = new Member(1L, "Aslam", "aslam@example.com");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(issueRepository.existsByBookIdAndMemberIdAndReturnDateIsNull(1L, 1L)).thenReturn(false);
        when(issueRepository.save(any(Issue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Call the real service
        IssueResponse response = issueService.issueBook(1L, 1L);

        // 3. Check the receipt
        assertThat(response.getBookTitle()).isEqualTo("1984");
        assertThat(response.getMemberName()).isEqualTo("Aslam");
        assertThat(response.getFine()).isEqualTo(0.0);
        assertThat(response.getDueDate()).isEqualTo(LocalDate.now().plusDays(14));

        // 4. Check that the shelf count dropped
        assertThat(book.getAvailableCopies()).isEqualTo(0);
        verify(bookRepository).save(book);
    }
    @Test
    void returnBook_lateReturn_calculatesFine() {

        // 1. Pretend: a slip that was due 4 days ago, not returned yet
        Book book = new Book(1L, "1984", "George Orwell", "Political", 0);
        Member member = new Member(1L, "Aslam", "aslam@example.com");
        Issue issue = new Issue(3L, book, member,
                LocalDate.now().minusDays(18),    // issued 18 days ago
                LocalDate.now().minusDays(4),     // due 4 days ago
                null,                             // not returned yet
                0.0);

        when(issueRepository.findById(3L)).thenReturn(Optional.of(issue));
        when(issueRepository.save(any(Issue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Call the real service
        IssueResponse response = issueService.returnBook(3L);

        // 3. Check the receipt
        assertThat(response.getFine()).isEqualTo(20.0);
        assertThat(response.getReturnDate()).isEqualTo(LocalDate.now());

        // 4. Check that the copy went back on the shelf
        assertThat(book.getAvailableCopies()).isEqualTo(1);
        verify(bookRepository).save(book);
    }
    @Test
    void returnBook_onTime_hasNoFine() {

        // 1. Pretend: a slip that is due in 3 days
        Book book = new Book(1L, "1984", "George Orwell", "Political", 0);
        Member member = new Member(1L, "Aslam", "aslam@example.com");
        Issue issue = new Issue(4L, book, member,
                LocalDate.now().minusDays(11),    // issued 11 days ago
                LocalDate.now().plusDays(3),      // due in 3 days
                null,
                0.0);

        when(issueRepository.findById(4L)).thenReturn(Optional.of(issue));
        when(issueRepository.save(any(Issue.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Call the real service
        IssueResponse response = issueService.returnBook(4L);

        // 3. Check: no fine, and the copy is back
        assertThat(response.getFine()).isEqualTo(0.0);
        assertThat(book.getAvailableCopies()).isEqualTo(1);
    }
}
