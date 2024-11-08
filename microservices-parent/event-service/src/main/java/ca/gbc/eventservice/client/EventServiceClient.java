package ca.gbc.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "event-service", url = "${event.service.url}") // URL can be injected from the properties file
public interface EventServiceClient {

    @GetMapping("/api/events/{eventId}")
    EventResponse getEventById(@PathVariable("eventId") Long eventId);

}
