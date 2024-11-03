package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

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
        log.debug("Attempting to create a booking: {}", bookingRequest.userId());

        // Check if room is available for the requested time slot
        if (!isRoomAvailable(bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime())) {
            log.warn("Room {} is already booked for the selected time slot.", bookingRequest.roomId());
            throw new IllegalArgumentException("Room " + bookingRequest.roomId() + " is not available for booking.");
        }

        Booking booking = Booking.builder()
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())
                .build();

        bookingRepository.save(booking);
        log.info("Booking {} created successfully", booking.getUserId()); // Updated here

        return new BookingResponse(
                booking.getUserId(),
                booking.getRoomId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getPurpose());
    }

    @Override
    public List<BookingResponse> getAllBooking() {
        log.debug("Fetching all booking");
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream().map(this::mapToBookingResponse).toList();
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getUserId(),
                booking.getRoomId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getPurpose()
        );
    }

    @Override
    public String updateBooking(String id, BookingRequest bookingRequest) {
        log.debug("Updating booking with id {}", id);

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(id)); // Match by userId or change to a unique ID field if necessary
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            // Ensure room availability for the updated time slot
            if (!isRoomAvailable(bookingRequest.roomId(),
                    bookingRequest.startTime(),
                    bookingRequest.endTime())) {
                throw new IllegalArgumentException("Room " + bookingRequest.roomId() + " is already booked for the updated time slot.");
            }

            booking.setUserId(bookingRequest.userId());
            booking.setRoomId(bookingRequest.roomId());
            booking.setStartTime(bookingRequest.startTime());
            booking.setEndTime(bookingRequest.endTime());
            booking.setPurpose(bookingRequest.purpose());

            return bookingRepository.save(booking).getUserId();
        }

        return id; // Return the ID if booking was not found
    }

    @Override
    public void deleteBooking(String id) {
        log.debug("Deleting booking with id {}", id);
        bookingRepository.deleteById(id);
    }

    @Override
    public boolean isRoomAvailable(String roomId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Checking availability for room {} between {} and {}", roomId, startTime, endTime);

        // Define query to find overlapping bookings
        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId)
                .andOperator(
                        Criteria.where("endTime").gt(startTime),
                        Criteria.where("startTime").lt(endTime)
                ));

        return mongoTemplate.find(query, Booking.class).isEmpty();
    }
}
