package ca.gbc.bookingservice.controller;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String makeBooking(@RequestBody BookingRequest bookingRequest) {
        bookingService.makeBooking(bookingRequest);
        return "Booking is successfully completed.";
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookingResponse> allBookings = bookingService.getAllBookings();
        return ResponseEntity.ok(allBookings);
    }

    @GetMapping("/getBookingEndTime")
    public LocalDateTime getBookingEndTime(@RequestParam("roomId") Long roomId) {

        System.out.println("Received request for roomId: " + roomId);
        Booking booking = bookingService.getLatestBookingByRoomId(roomId);
        return booking.getEndTime();
    }
}
