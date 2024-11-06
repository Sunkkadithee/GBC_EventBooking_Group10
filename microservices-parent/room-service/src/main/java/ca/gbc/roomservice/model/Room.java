
package ca.gbc.roomservice.model;

import jakarta.persistence.*;
import lombok.*;

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
    private Long Id;
    private String roomName;
    private Integer capacity;
    private String feature;
    private Boolean availability;
}
