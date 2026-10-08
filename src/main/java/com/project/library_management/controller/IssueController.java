package com.project.library_management.controller;

import com.project.library_management.DTO.IssueRequest;
import com.project.library_management.DTO.IssueResponse;
import com.project.library_management.service.IssueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueResponse issueBook(@Valid @RequestBody IssueRequest request) {
        return issueService.issueBook(request.getBookId(), request.getMemberId());
    }

    @PostMapping("/{id}/return")
    public IssueResponse returnBook(@PathVariable Long id) {
        return issueService.returnBook(id);
    }
}