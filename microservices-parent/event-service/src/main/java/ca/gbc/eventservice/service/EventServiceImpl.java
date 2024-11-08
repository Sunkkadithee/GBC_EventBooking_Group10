package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public Event createEvent(EventRequest eventRequest) {
        Event event = new Event(
                eventRequest.eventName(),
                eventRequest.organizerId(),
                eventRequest.eventType(),
                eventRequest.expectedAttendees(),
                eventRequest.eventStart(),
                eventRequest.eventEnd(),
                eventRequest.roomBookingId()
        );
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event updateEvent(String id, EventRequest eventRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        event.setEventName(eventRequest.eventName());
        event.setOrganizerId(eventRequest.organizerId());
        event.setEventType(eventRequest.eventType());
        event.setExpectedAttendees(eventRequest.expectedAttendees());
        event.setEventStart(eventRequest.eventStart());
        event.setEventEnd(eventRequest.eventEnd());
        event.setRoomBookingId(eventRequest.roomBookingId());
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }
}
