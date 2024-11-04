package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingRequest(
        String userId,
        Long roomId, // Change to Long
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
