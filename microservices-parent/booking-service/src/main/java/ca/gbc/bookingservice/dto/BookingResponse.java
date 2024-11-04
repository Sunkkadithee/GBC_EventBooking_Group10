package ca.gbc.bookingservice.dto;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record BookingResponse(

        String id,
        String userId,
        Long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose
) {
}
