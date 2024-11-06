package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final RoomClient roomClient;

    @Override
    public void createBooking(BookingRequest bookingRequest) {
        try {
            // Convert roomId to String before passing to RoomClient
            String roomId = String.valueOf(bookingRequest.roomId());

            // Fetch room availability from RoomService using the roomId from bookingRequest
            boolean isAvailable = roomClient.isAvailable(roomId, true); // Availability check

            // If room is not available, throw an exception
            if (!isAvailable) {
                throw new RuntimeException("Room " + roomId + " is not available.");
            }

            // Create a booking record
            Booking booking = Booking.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(bookingRequest.userId())
                    .roomId(Long.valueOf(roomId))
                    .startTime(bookingRequest.startTime())
                    .endTime(bookingRequest.endTime())
                    .purpose(bookingRequest.purpose())
                    .build();

            // Save the booking to the repository
            bookingRepository.save(booking);

            log.info("Booking created successfully for user: {}", bookingRequest.userId());
        } catch (Exception e) {
            log.error("Error occurred while creating booking: ", e);
            throw new RuntimeException("Failed to create booking: " + e.getMessage(), e);
        }
    }
}
