package ca.gbc.roomservice.dto;

public record RoomRequest(
        Long id,
        String roomName,
        Integer capacity,
        String feature,
        Boolean availability
) { }
