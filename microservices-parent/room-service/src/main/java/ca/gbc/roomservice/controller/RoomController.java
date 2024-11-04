package ca.gbc.roomservice.controller;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService; // Correctly refer to the RoomService

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addRoom(@RequestBody RoomRequest roomRequest) {
        return roomService.addRoom(roomRequest); // Return the message from the service
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        roomService.updateRoom(id, roomRequest);
        return "Room updated successfully";
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
    }
}