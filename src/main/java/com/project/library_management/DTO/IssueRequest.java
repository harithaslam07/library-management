package com.project.library_management.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IssueRequest {

    @NotNull(message = "bookId is required")
    private Long bookId;

    @NotNull(message = "memberId is required")
    private Long memberId;
}