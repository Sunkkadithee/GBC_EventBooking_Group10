package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public String addRoom(RoomRequest roomRequest) {
        // Check if the room with the same name already exists
        if (roomRepository.existsByRoomName(roomRequest.roomName())) {
            return "Room with name " + roomRequest.roomName() + " already exists.";
        }

        // Build the Room entity from the RoomRequest DTO
        Room room = Room.builder()
                .roomName(roomRequest.roomName())
                .capacity(roomRequest.capacity())
                .availability(roomRequest.availability())
                .feature(roomRequest.feature())
                .build();

        // Save the room to the repository
        roomRepository.save(room);
        log.info("Room added successfully: {}", room);
        return "Room added successfully"; // Return success message
    }

    @Override
    public void updateRoom(Long id, RoomRequest roomRequest) {
        // Fetch the existing room
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        // Update the fields of the existing room
        existingRoom.setRoomName(roomRequest.roomName());
        existingRoom.setCapacity(roomRequest.capacity());
        existingRoom.setAvailability(roomRequest.availability());
        existingRoom.setFeature(roomRequest.feature());

        // Save the updated room
        roomRepository.save(existingRoom);
        log.info("Room updated successfully: {}", existingRoom);
    }

    @Override
    public void deleteRoom(Long id) {
        // Check if room exists before deletion
        if (!roomRepository.existsById(id)) {
            throw new RuntimeException("Room not found with id: " + id);
        }
        roomRepository.deleteById(id);
        log.info("Room deleted successfully with id: {}", id);
    }
}
