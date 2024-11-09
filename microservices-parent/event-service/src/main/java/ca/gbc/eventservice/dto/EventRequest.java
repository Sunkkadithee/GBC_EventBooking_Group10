package ca.gbc.eventservice.dto;

import java.time.LocalDateTime;

public record EventRequest(
        String eventName,
        String organizerId,
        String eventType,
        Integer expectedAttendees,
        LocalDateTime eventStart,
        LocalDateTime eventEnd,
        String roomBookingId) {
}
