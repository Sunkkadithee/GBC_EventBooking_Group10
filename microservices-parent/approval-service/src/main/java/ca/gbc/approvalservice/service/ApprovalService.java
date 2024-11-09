package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;

public interface ApprovalService {

    // Method to approve or reject an event request
    ApprovalResponse processApproval(ApprovalRequest approvalRequest);
}
