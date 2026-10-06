package com.project.library_management.repository;

import com.project.library_management.model.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    List<Issue> findByMemberId(Long memberId);

    List<Issue> findByReturnDateIsNull();

    boolean existsByBookIdAndMemberIdAndReturnDateIsNull(Long bookId, Long memberId);
}