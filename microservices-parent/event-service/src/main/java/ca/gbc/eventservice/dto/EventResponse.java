package ca.gbc.eventservice.dto;

import java.time.LocalDateTime;

public record EventResponse(
        String id,
        String eventName,
        String organizerId,
        String eventType,
        Integer expectedAttendees,
        LocalDateTime eventStart,
        LocalDateTime eventEnd,
        String roomBookingId) {
}
