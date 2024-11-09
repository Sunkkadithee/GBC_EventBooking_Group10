package ca.gbc.roomservice.dto;

import java.math.BigDecimal;

public record RoomRequest(
        Long id,
        String roomName,
        Integer capacity,
        String features,
        BigDecimal price,
        boolean availability
) {
}
