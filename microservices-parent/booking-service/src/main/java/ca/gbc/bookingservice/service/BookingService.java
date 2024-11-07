package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;

import java.util.List;


public interface BookingService {
    void makeBooking(BookingRequest bookingRequest);
    List<BookingResponse> getAllBookings();
    String updateBooking(String id, BookingRequest bookingRequest);
    void cancelBooking(String id);
    Booking getLatestBookingByRoomId(Long roomId);
}
