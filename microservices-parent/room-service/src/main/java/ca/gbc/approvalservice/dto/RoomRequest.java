package ca.gbc.approvalservice.dto;

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
