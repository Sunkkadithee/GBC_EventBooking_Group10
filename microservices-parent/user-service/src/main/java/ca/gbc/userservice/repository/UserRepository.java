package ca.gbc.userservice.repository;

import ca.gbc.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by their email
    Optional<User> findByEmail(String email);

    // Custom query method to find a user by their role
    Optional<User> findByRole(String role);
}
