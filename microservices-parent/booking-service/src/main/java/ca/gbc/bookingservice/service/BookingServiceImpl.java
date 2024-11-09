package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.dto.RoomResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import com.mongodb.client.MongoClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final BookingRepository bookingRepository; // Booking repository
    private final MongoTemplate mongoTemplate; // MongoDB template for queries
    private final MongoClient mongo; // Mongo client for raw access (optional in many cases)
    @Autowired
    private final RoomClient roomClient; // Room service client (via Feign)

    @Override
    public void makeBooking(BookingRequest bookingRequest) {

        // Fetch room details from RoomService via Feign client
        RoomResponse roomResponse = roomClient.getRoomById(bookingRequest.roomId());

        // Check if the room is available
        if (!roomResponse.availability()) {
            throw new IllegalArgumentException("Room is not available for booking.");
        }

        // Proceed with booking if room is available
        Booking booking = Booking.builder()
                .roomId(bookingRequest.roomId())
                .userName(bookingRequest.userName())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())  // Added purpose here
                .status(bookingRequest.status())
                .build();

        // Save the booking to the repository
        bookingRepository.save(booking);

        // Call RoomClient to update the availability of the room
        roomClient.updateRoomAvailabilityBasedOnBookingEnd(
                bookingRequest.roomId(),
                bookingRequest.endTime()
        );
    }

    public Booking getLatestBookingByRoomId(Long roomId) {
        return bookingRepository.findFirstByRoomIdOrderByEndTimeDesc(roomId)
                .orElseThrow(() -> new RuntimeException("Booking not found for roomId: " + roomId));
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        log.debug("Getting all bookings");
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream().map(this::mapToBookingResponse).toList();
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getId(),
                booking.getRoomId(),
                booking.getUserName(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getPurpose(),
                booking.getStatus()
        );
    }

    @Override
    public String updateBooking(String id, BookingRequest bookingRequest) {
        log.debug("Updating booking request {}", id);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            booking.setUserName(bookingRequest.userName());
            booking.setStartTime(bookingRequest.startTime());
            booking.setEndTime(bookingRequest.endTime());
            booking.setPurpose(bookingRequest.purpose());  // Update purpose here
            booking.setStatus(bookingRequest.status());
            return bookingRepository.save(booking).getId();
        }
        return id + " not found";
    }



    @Override
    public void cancelBooking(String id) {
        log.debug("Cancelling booking request {}", id);

        bookingRepository.deleteById(id); // Assuming deleteById expects String id
    }


}
