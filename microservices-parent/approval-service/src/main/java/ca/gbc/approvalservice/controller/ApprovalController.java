package ca.gbc.approvalservice.controller;


import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/process")
    public ApprovalResponse processApproval(@RequestBody ApprovalRequest request) {
        return approvalService.processApproval(request);
    }
}