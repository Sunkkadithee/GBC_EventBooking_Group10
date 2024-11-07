package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {
    RoomResponse checkRoomAvailability(RoomRequest roomRequest);
    RoomResponse createRoomAdmin(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    public boolean isRoomAvailable(Long id);
    boolean updateRoomAvailability(Long roomId, LocalDateTime bookingEnd);
    RoomResponse getRoomById(Long roomId);
}
