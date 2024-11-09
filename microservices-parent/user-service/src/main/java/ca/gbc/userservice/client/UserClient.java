package ca.gbc.userservice.client;

import ca.gbc.userservice.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service", url = "http://localhost:8081")  // The URL of the RoomService
public interface UserClient {

    // Define methods to interact with RoomService, for example, fetching room availability for a user
    @GetMapping("/api/rooms/{userId}")
    UserResponse getUserRoomDetails(@PathVariable Long userId);
}
