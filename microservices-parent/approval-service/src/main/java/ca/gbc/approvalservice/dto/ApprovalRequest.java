package ca.gbc.approvalservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ApprovalRequest(
        Long approvalId,
        boolean isApproved,
        LocalDateTime approvalDate
) {
}
