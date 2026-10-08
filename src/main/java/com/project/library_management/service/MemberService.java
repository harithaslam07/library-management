package com.project.library_management.service;

import com.project.library_management.DTO.MemberRequest;
import com.project.library_management.DTO.MemberResponse;
import com.project.library_management.exception.ConflictException;
import com.project.library_management.exception.ResourceNotFoundException;
import com.project.library_management.model.Member;
import com.project.library_management.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse addMember(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered: " + request.getEmail());
        }
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        return MemberResponse.from(memberRepository.save(member));
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse getMember(Long id) {
        return MemberResponse.from(findMember(id));
    }

    private Member findMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found: " + id));
    }
}