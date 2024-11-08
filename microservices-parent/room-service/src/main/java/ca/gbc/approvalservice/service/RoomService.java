package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.RoomRequest;
import ca.gbc.approvalservice.dto.RoomResponse;

import java.time.LocalDateTime;
import java.util.List;


public interface RoomService {
    RoomResponse checkRoomAvailability(RoomRequest roomRequest);
    RoomResponse createRoomAdmin(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    public boolean isRoomAvailable(Long id);
    //    RoomResponse getRoomById(Long roomId);
    boolean updateRoomAvailability(Long roomId, LocalDateTime bookingEnd);
    RoomResponse getRoomById(Long roomId);
}