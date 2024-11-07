package ca.gbc.roomservice.service;

import ca.gbc.roomservice.client.RoomClient;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@Transactional
@Service
public class RoomAvailabilityService {

    private final RoomRepository roomRepository;
    private final RoomClient roomClient;

    @Autowired
    public RoomAvailabilityService(RoomRepository roomRepository, RoomClient roomClient) {
        this.roomRepository = roomRepository;
        this.roomClient = roomClient;
    }

    // This method will be executed every minute to check for rooms to update their availability
    @Scheduled(fixedRate = 60000) // 1 minute
    public void checkAndUpdateRoomAvailability() {
        List<Room> rooms = roomRepository.findAllBookedRooms(); // Retrieve all rooms or use the appropriate query to get the list of rooms

        for (Room room : rooms) {
            // Fetch the booking end time for each room from the Booking microservice using RoomClient
            LocalDateTime bookingEndTime = roomClient.getBookingEndTime(room.getId());

            if (bookingEndTime != null && bookingEndTime.isBefore(LocalDateTime.now())) {
                // If the booking end time has passed, update the room's availability
                room.setAvailability(true); // Correct setter call
                roomRepository.save(room); // Save the updated room

                System.out.println("Room " + room.getId() + " availability updated at " + LocalDateTime.now());
            }
        }
    }

}
