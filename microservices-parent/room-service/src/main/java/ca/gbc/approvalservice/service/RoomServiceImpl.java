package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.RoomRequest;
import ca.gbc.approvalservice.dto.RoomResponse;
import ca.gbc.approvalservice.model.Room;
import ca.gbc.approvalservice.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional



public class RoomServiceImpl implements RoomService{

    @Autowired
    private final RoomRepository roomRepository;

    // Ramtin's code
    @Override
    public boolean isRoomAvailable(Long roomId) {
        return roomRepository.existsByIdAndAvailabilityIsTrue(roomId);
    }


    @Override
    public RoomResponse getRoomById(Long roomId) {
        // Assume there's a repository or a way to fetch the room details by ID
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Convert Room entity to RoomResponse DTO
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.getPrice(),
                room.isAvailability()
        );
    }


    @Override
    public boolean updateRoomAvailability(Long roomId, LocalDateTime bookingEnd) {
        // Fetch the room by roomId
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        // Check if the booking end time is in the past
        if (bookingEnd.isBefore(LocalDateTime.now())) {
            // Set the room availability to false (booked)
            room.setAvailability(false);

            // Save the updated room
            roomRepository.save(room);
            return true;
        }

        // Return false if the booking end is not in the past
        return false;
    }


    @Override
    public RoomResponse checkRoomAvailability(RoomRequest roomRequest) {
        log.debug("Checking room availability{}", roomRequest.roomName());

        Room room = Room.builder()
                .roomName(roomRequest.roomName())
                .features(roomRequest.features())
                .capacity(roomRequest.capacity())
                .price(roomRequest.price())
                .availability(roomRequest.availability())
                .build();

        roomRepository.save(room);

        log.debug("Room availability checked successfully");
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.getPrice(),
                room.isAvailability()
        );

    }

    public RoomResponse createRoomAdmin(RoomRequest roomRequest) {
        log.debug("Creating room admin {}", roomRequest.roomName());
        Room room = Room.builder()
                .roomName(roomRequest.roomName())
                .features(roomRequest.features())
                .capacity(roomRequest.capacity())
                .price(roomRequest.price())
                .availability(roomRequest.availability())
                .build();

        roomRepository.save(room);
        log.debug("Room created successfully by admin");
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.getPrice(),
                room.isAvailability()

        );
    }

    public List<RoomResponse>
    getAllRooms() {
        log.debug("Getting all rooms");
        List<Room> roomResponses = roomRepository.findAll();

        return roomResponses.stream().map(this::mapToRoomResponse).toList();

    }


    private RoomResponse mapToRoomResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.getPrice(),
                room.isAvailability()
        );
    }
}