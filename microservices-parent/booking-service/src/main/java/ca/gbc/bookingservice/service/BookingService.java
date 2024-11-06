package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
   void createBooking(BookingRequest bookingRequest);
    //List<BookingResponse> getAllBooking();
   // void updateBooking(String bookingId, BookingRequest bookingRequest);
    //void deleteBooking(String bookingId);


}
