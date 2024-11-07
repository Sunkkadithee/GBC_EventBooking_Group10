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
    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final MongoClient mongo;
    @Autowired
    private final RoomClient roomClient;

    @Override
    public void makeBooking(BookingRequest bookingRequest) {

        // Fetch room details from RoomService via Feign client
        RoomResponse roomResponse = roomClient.getRoomById(bookingRequest.roomId());

        // Check if the room is available
        if (!roomResponse.availability()) {
            throw new IllegalArgumentException("Room is not available for booking.");
        }

        // Check if the number of people exceeds room capacity
        if (bookingRequest.numberOfPeople() > roomResponse.capacity()) {
            throw new IllegalArgumentException("The number of people exceeds room capacity.");
        }

        // Proceed with booking if room is available and capacity is sufficient
        Booking booking = Booking.builder()
                .roomId(bookingRequest.roomId())
                .bookHolderName(bookingRequest.bookHolderName())
                .numberOfPeople(bookingRequest.numberOfPeople())
                .bookingStart(bookingRequest.bookingStart())
                .bookingEnd(bookingRequest.bookingEnd())
                .status(bookingRequest.status())
                .build();

        // Save the booking to the repository
        bookingRepository.save(booking);

        // Call RoomClient to update the availability of the room
        roomClient.updateRoomAvailabilityBasedOnBookingEnd(
                bookingRequest.roomId(),
                bookingRequest.bookingEnd()
        );
    }

    //    @Override
//    public Booking getBookingByRoomId(Long roomId) {
//        // Query the repository to find the booking by roomId
//        return bookingRepository.findByRoomId(roomId)
//                .orElseThrow(() -> new RuntimeException("Booking not found for roomId: " + roomId));
//    }
    public Booking getLatestBookingByRoomId(Long roomId) {
        return bookingRepository.findFirstByRoomIdOrderByBookingEndDesc(roomId)
                .orElseThrow(() -> new RuntimeException("Booking not found for roomId: " + roomId));
    }



    @Override
    public List<BookingResponse> getAllBookings(){
        log.debug("Getting all bookings");
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream().map(this::mapToBookingResponse).toList();

    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(booking.getId(), booking.getRoomId(), booking.getBookHolderName(),
                booking.getNumberOfPeople(), booking.getBookingStart(), booking.getBookingEnd(),
                booking.getStatus());
    }


    @Override
    public String updateBooking(String id, BookingRequest bookingRequest) {
        log.debug("Updating booking request {}", id);

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking != null) {
            booking.setBookHolderName(bookingRequest.bookHolderName());
            booking.setNumberOfPeople(bookingRequest.numberOfPeople());
            booking.setBookingStart(bookingRequest.bookingStart());
            booking.setBookingEnd(bookingRequest.bookingEnd());
            booking.setStatus(bookingRequest.status());
            return bookingRepository.save(booking).getId();
        }
        return id + " not found";
    }

    @Override
    public void cancelBooking(String id) {
        log.debug("Cancelling booking request {}", id);
        bookingRepository.deleteById(id);
    }

}
