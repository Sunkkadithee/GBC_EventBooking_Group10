
package ca.gbc.approvalservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "t_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomName;
    private Integer capacity;
    private String features; // e.g. projector, whiteboard
    private BigDecimal price;
    private boolean availability; // true or false
}