
package ca.gbc.approvalservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_approvals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;  // Assuming you have an Event class

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;    // Assuming you have a User class

    private boolean isApproved;

    private LocalDateTime approvalDate;

    // Getters and Setters
}