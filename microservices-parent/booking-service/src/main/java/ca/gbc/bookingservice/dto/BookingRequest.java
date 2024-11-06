package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingRequest(
        String id,
        String userId,
        long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) { }
