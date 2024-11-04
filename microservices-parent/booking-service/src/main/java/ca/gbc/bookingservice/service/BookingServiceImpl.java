package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.client.RoomServiceClient; // Import RoomServiceClient
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final RoomServiceClient roomServiceClient; // Inject RoomServiceClient

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        log.debug("Attempting to create a booking for user: {}", bookingRequest.userId());

        // Check if the room is available from RoomService
        if (!roomServiceClient.isRoomAvailable(bookingRequest.roomId())) {
            log.warn("Room {} is not available for booking.", bookingRequest.roomId());
            throw new IllegalArgumentException("Room " + bookingRequest.roomId() + " is not available for booking.");
        }

        // Create Booking entity
        Booking booking = Booking.builder()
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())
                .build();

        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking.getId(), savedBooking.getUserId(), savedBooking.getRoomId(),
                savedBooking.getStartTime(), savedBooking.getEndTime(), savedBooking.getPurpose());
    }

    @Override
    public List<BookingResponse> getAllBooking() {
        log.debug("Fetching all bookings.");
        return bookingRepository.findAll().stream()
                .map(booking -> new BookingResponse(booking.getId(), booking.getUserId(), booking.getRoomId(),
                        booking.getStartTime(), booking.getEndTime(), booking.getPurpose()))
                .toList();
    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Updating booking with id: {}", bookingId);

        // Check if the booking exists
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // Check if the room is available from RoomService
        if (!roomServiceClient.isRoomAvailable(bookingRequest.roomId())) {
            log.warn("Room {} is not available for booking.", bookingRequest.roomId());
            throw new IllegalArgumentException("Room " + bookingRequest.roomId() + " is not available for booking.");
        }

        // Update the booking fields
        booking.setUserId(bookingRequest.userId());
        booking.setRoomId(bookingRequest.roomId());
        booking.setStartTime(bookingRequest.startTime());
        booking.setEndTime(bookingRequest.endTime());
        booking.setPurpose(bookingRequest.purpose());

        // Save updated booking
        bookingRepository.save(booking);
        return booking.getId();
    }

    @Override
    public void deleteBooking(String bookingId) {
        log.debug("Deleting booking with id: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        // Logic to check if the room is available for the given time period
        return roomServiceClient.isRoomAvailable(roomId); // Assuming roomId is now of type Long
    }
}
