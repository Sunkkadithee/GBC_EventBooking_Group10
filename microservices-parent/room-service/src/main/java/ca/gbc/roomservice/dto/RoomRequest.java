package ca.gbc.roomservice.dto;

public record RoomRequest(
        Long Id,
        String roomName,
        Integer capacity,
        String feature,
        Boolean availability
) { }
