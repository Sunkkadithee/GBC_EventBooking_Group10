package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.model.Event;
import ca.gbc.approvalservice.model.User;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import ca.gbc.approvalservice.repository.EventRepository;
import ca.gbc.approvalservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public ApprovalServiceImpl(ApprovalRepository approvalRepository,
                               EventRepository eventRepository,
                               UserRepository userRepository,
                               EventService eventService,
                               UserService userService) {
        this.approvalRepository = approvalRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public ApprovalResponse processApproval(ApprovalRequest approvalRequest) {
        // Fetch event details using EventService (or directly if needed)
        Event event = eventService.getEventById(approvalRequest.eventId());
        if (event == null) {
            throw new IllegalArgumentException("Event not found with ID: " + approvalRequest.eventId());
        }

        // Fetch user details using UserService
        User user = userService.getUserById(approvalRequest.userId());
        if (user == null) {
            throw new IllegalArgumentException("User not found with ID: " + approvalRequest.userId());
        }

        // Check if user has the correct privileges to approve
        if (!userService.hasApprovalPrivileges(user, event)) {
            throw new IllegalStateException("User does not have the correct privileges to approve this event.");
        }

        // Create the approval entry and save it
        Approval approval = new Approval(event, user, approvalRequest.approvalStatus(), LocalDateTime.now());
        approvalRepository.save(approval);

        // Return the response
        return new ApprovalResponse(approval.getId(), approval.isApproved(), approval.getApprovalDate());
    }
}
