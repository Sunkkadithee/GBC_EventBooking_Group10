package ca.gbc.roomservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@FeignClient(name = "booking", url="${booking.service.url}")
public interface RoomClient {

    // Fetch the booking end time for a specific room
    @RequestMapping(method = RequestMethod.GET, value = "/api/booking/getBookingEndTime")
    LocalDateTime getBookingEndTime(@RequestParam("roomId") Long roomId);
}
