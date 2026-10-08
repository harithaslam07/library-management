package com.project.library_management.controller;

import com.project.library_management.DTO.MemberRequest;
import com.project.library_management.DTO.MemberResponse;
import com.project.library_management.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse add(@Valid @RequestBody MemberRequest request) {
        return memberService.addMember(request);
    }

    @GetMapping
    public List<MemberResponse> getAll() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public MemberResponse getOne(@PathVariable Long id) {
        return memberService.getMember(id);
    }
}