package com.project.library_management.controller;

import com.project.library_management.model.Member;
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
    public Member add(@Valid @RequestBody Member member) {
        return memberService.addMember(member);
    }

    @GetMapping
    public List<Member> getAll() {
        return memberService.getAllMembers();
    }

    @GetMapping("/{id}")
    public Member getOne(@PathVariable Long id) {
        return memberService.getMember(id);
    }
}