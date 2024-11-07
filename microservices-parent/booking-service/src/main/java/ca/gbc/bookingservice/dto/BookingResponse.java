package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        String id,
        Long roomId,
        String bookHolderName,

        Integer numberOfPeople,

        LocalDateTime bookingStart,

        LocalDateTime bookingEnd,

        String status) {

}
