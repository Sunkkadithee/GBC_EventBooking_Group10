package ca.gbc.eventservice.controller;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    // Create a new event
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {
        Event event = eventService.createEvent(eventRequest);
        EventResponse eventResponse = mapToEventResponse(event);
        return ResponseEntity.ok(eventResponse);
    }

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventResponse> eventResponses = events.stream()
                .map(this::mapToEventResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventResponses);
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable String id) {
        Optional<Event> event = eventService.getEventById(id);
        return event.map(e -> ResponseEntity.ok(mapToEventResponse(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update event by ID
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable String id, @RequestBody EventRequest eventRequest) {
        Event updatedEvent = eventService.updateEvent(id, eventRequest);
        EventResponse eventResponse = mapToEventResponse(updatedEvent);
        return ResponseEntity.ok(eventResponse);
    }

    // Delete event by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Helper method to map Event to EventResponse
    private EventResponse mapToEventResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getEventName(),
                event.getOrganizerId(),
                event.getEventType(),
                event.getExpectedAttendees(),
                event.getEventStart(),
                event.getEventEnd(),
                event.getRoomBookingId()
        );
    }
}
