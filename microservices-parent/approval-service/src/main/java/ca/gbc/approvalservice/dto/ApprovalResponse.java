package ca.gbc.approvalservice.dto;

import java.time.LocalDateTime;
public record ApprovalResponse(
        Long approvalId,
        boolean isApproved,
        LocalDateTime approvalDate
) {
}