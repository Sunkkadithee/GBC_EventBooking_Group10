package ca.gbc.approvalservice.repository;

import ca.gbc.approvalservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    boolean existsByIdAndAvailabilityIsTrue(Long id);
    Optional<Room> findById(Long roomId);

    @Query("SELECT r FROM Room r WHERE r.availability = false")
    List<Room> findAllBookedRooms();

}