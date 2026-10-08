package com.project.library_management.DTO;

import com.project.library_management.model.Member;
import lombok.*;

@Getter
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String name;
    private String email;

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}