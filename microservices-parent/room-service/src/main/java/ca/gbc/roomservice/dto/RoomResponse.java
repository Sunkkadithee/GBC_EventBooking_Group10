package ca.gbc.roomservice.dto;

public record RoomResponse(
        Long  Id,
        String roomName,
        Integer capacity,
        String feature,
        Boolean availability
) { }

