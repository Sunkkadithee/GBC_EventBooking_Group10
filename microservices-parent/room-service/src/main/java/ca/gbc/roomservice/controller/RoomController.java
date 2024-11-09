package ca.gbc.roomservice.controller;


import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.service.RoomAvailabilityService;
import ca.gbc.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/roomBooking")
@RequiredArgsConstructor

public class RoomController {

    @Autowired
    private final RoomService roomService;
    private final RoomAvailabilityService roomAvailabilityService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public boolean isRoomAvailable(Long id) { return roomService.isRoomAvailable(id);}

    @GetMapping("/roomDetails")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RoomResponse> getRoomById(@RequestParam Long roomId) {
        RoomResponse roomResponse = roomService.getRoomById(roomId);
        return ResponseEntity.ok(roomResponse);
    }


    // Endpoint to update room availability based on the booking end time
    @PutMapping("/updateAvailability")
    @ResponseStatus(HttpStatus.OK)
    public boolean updateRoomAvailabilityBasedOnBookingEnd(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime bookingEnd
    ) {
        // Call RoomService to update room availability
        return roomService.updateRoomAvailability(roomId, bookingEnd);
    }


    @PutMapping("/api/roomBooking/constantUpdate")
    public void updateRoomAvailability(@RequestParam Long id, @RequestParam LocalDateTime bookingEnd) {
        roomAvailabilityService.checkAndUpdateRoomAvailability(); // Manually trigger the availability check
    }



    @PostMapping("/createRoomAdmin")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoomResponse> createRoomAdmin(@RequestBody RoomRequest roomRequest) {
        RoomResponse newRoomAdmin = roomService.createRoomAdmin(roomRequest);
        return ResponseEntity.ok(newRoomAdmin);
    }

    @GetMapping("/allRooms")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> allRooms = roomService.getAllRooms();
        return ResponseEntity.ok(allRooms);
    }


}