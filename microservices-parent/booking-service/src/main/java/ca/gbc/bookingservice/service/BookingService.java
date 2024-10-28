package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);

    List<BookingResponse> getAllBooking();
    String updateBooking(String id, BookingRequest bookingRequest);// Update an existing booking
    void deleteBooking(String id);// Delete a booking by ID

    // Check for double-booking based on room and time slot
    boolean isRoomAvailable(String roomId, LocalDateTime startTime, LocalDateTime endTime);
}
