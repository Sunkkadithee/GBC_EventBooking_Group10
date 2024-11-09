package ca.gbc.bookingservice.repository;

import ca.gbc.bookingservice.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findAllByEndTimeBeforeAndStatusEquals(LocalDateTime endTime, String status);
    Optional<Booking> findByRoomId(Long roomId);
    Optional<Booking> findFirstByRoomIdOrderByEndTimeDesc(Long roomId);
}
