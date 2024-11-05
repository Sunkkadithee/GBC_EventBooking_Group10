package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        log.debug("Attempting to create a booking for user: {}", bookingRequest.userId());

        // Check room availability
        if (!isRoomAvailable(Long.parseLong(bookingRequest.roomId()), bookingRequest.startTime(), bookingRequest.endTime())) {
            log.warn("Room {} is not available for the specified time period.", bookingRequest.roomId());
            throw new IllegalArgumentException("Room " + bookingRequest.roomId() + " is not available for the specified time.");
        }

        // Create and save booking
        Booking booking = Booking.builder()
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        return new BookingResponse(savedBooking.getId(), savedBooking.getUserId(), savedBooking.getRoomId(),
                savedBooking.getStartTime(), savedBooking.getEndTime(), savedBooking.getPurpose());
    }

    @Override
    public List<BookingResponse> getAllBooking() {
        log.debug("Fetching all bookings.");
        return bookingRepository.findAll().stream()
                .map(booking -> new BookingResponse(booking.getId(), booking.getUserId(), booking.getRoomId(),
                        booking.getStartTime(), booking.getEndTime(), booking.getPurpose()))
                .toList();
    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Updating booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        if (!isRoomAvailable(Long.parseLong(bookingRequest.roomId()), bookingRequest.startTime(), bookingRequest.endTime())) {
            log.warn("Room {} is not available for the specified time period.", bookingRequest.roomId());
            throw new IllegalArgumentException("Room " + bookingRequest.roomId() + " is not available for the specified time.");
        }

        booking.setUserId(bookingRequest.userId());
        booking.setRoomId(bookingRequest.roomId());
        booking.setStartTime(bookingRequest.startTime());
        booking.setEndTime(bookingRequest.endTime());
        booking.setPurpose(bookingRequest.purpose());

        bookingRepository.save(booking);
        return booking.getId();
    }

    @Override
    public void deleteBooking(String bookingId) {
        log.debug("Deleting booking with id: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId)
                .andOperator(
                        Criteria.where("endTime").gt(startTime),
                        Criteria.where("startTime").lt(endTime)
                ));
        return mongoTemplate.find(query, Booking.class).isEmpty();
    }
}
