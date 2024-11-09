package ca.gbc.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String role;

    @Enumerated(EnumType.STRING)
    private UserType userType; // Enum for user types

    public enum UserType {
        STUDENT,
        STAFF,
        FACULTY
    }
}
