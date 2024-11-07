package ca.gbc.bookingservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String roomName,
        Integer capacity,
        String features,
        BigDecimal price,
        boolean availability
) {
}
