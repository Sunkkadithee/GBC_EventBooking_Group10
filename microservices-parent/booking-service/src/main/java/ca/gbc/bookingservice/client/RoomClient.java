package ca.gbc.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "room", url = "${room.service.url}")
public interface RoomClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/room")
    boolean isAvailable(@RequestParam String id, @RequestParam Boolean availability);
}
