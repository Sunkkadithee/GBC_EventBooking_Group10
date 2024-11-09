package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event createEvent(EventRequest eventRequest);
    List<Event> getAllEvents();
    Optional<Event> getEventById(String id);
    Event updateEvent(String id, EventRequest eventRequest);
    void deleteEvent(String id);
}
