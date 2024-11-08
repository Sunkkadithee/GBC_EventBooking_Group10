package ca.gbc.bookingservice.client;

import ca.gbc.bookingservice.dto.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
@FeignClient(value="room", url="${room.service.url}")
public interface RoomClient {


    @RequestMapping(method = RequestMethod.GET, value = "/api/roomBooking")
    boolean isRoomAvailable(@RequestParam Long id);

    @RequestMapping(method=RequestMethod.GET, value= "/api/roomBooking/roomDetails")
    RoomResponse getRoomById(@RequestParam Long roomId);

    // Method to update room availability based on the booking end time
    @RequestMapping(method=RequestMethod.PUT, value = "/api/roomBooking/updateAvailability")
    boolean updateRoomAvailabilityBasedOnBookingEnd(@RequestParam Long roomId, @RequestParam LocalDateTime bookingEnd);


}
