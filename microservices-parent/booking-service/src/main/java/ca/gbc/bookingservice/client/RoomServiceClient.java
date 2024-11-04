package ca.gbc.bookingservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

@Component
public class RoomServiceClient {

    private final RestTemplate restTemplate;

    @Value("${room.service.url}") // Ensure you set this property in your application.properties
    private String roomServiceUrl;

    public RoomServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean isRoomAvailable(Long roomId) {
        // This example assumes the Room Service has an endpoint to check availability
        String url = roomServiceUrl + "/api/rooms/" + roomId + "/availability";

        // Call the Room Service API and handle the response
        try {
            // Assume this endpoint returns a boolean indicating availability
            return restTemplate.getForObject(url, Boolean.class);
        } catch (Exception e) {
            // Log error and return false or handle accordingly
            return false;
        }
    }
}
