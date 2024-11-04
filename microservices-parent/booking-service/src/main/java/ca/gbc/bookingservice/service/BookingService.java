package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    List<BookingResponse> getAllBooking();
    String updateBooking(String bookingId, BookingRequest bookingRequest);
    void deleteBooking(String bookingId);

    // Update the method signature here
    boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime);
}
