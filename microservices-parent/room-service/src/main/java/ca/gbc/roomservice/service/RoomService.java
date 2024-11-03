package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;

public interface RoomService {
    String addRoom(RoomRequest roomRequest); // Change return type to String
    void updateRoom(Long id, RoomRequest roomRequest);
    void deleteRoom(Long id);
}
