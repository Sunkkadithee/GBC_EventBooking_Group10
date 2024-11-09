package ca.gbc.approvalservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ca.gbc.approvalservice.model.Approval;

public interface ApprovalRepository extends JpaRepository<Approval, Long> {
    // Custom query to find approval by event and user
    Approval findByEventIdAndUserId(Long eventId, Long userId);
}
