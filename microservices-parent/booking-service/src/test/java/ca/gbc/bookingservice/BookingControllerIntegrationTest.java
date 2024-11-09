package ca.gbc.bookingservice;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        BookingResponse mockBookingResponse = new BookingResponse("user123", "room123", LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Study Session");
        List<BookingResponse> mockBookingResponses = Collections.singletonList(mockBookingResponse);
        when(bookingService.getAllBookings()).thenReturn(mockBookingResponses);
    }

    @Test
    public void testMakeBooking() throws Exception {
        BookingRequest bookingRequest = new BookingRequest("user123", "room123", LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2), "Study Session");

        mockMvc.perform(post("/api/booking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Booking is successfully completed."));
    }

    @Test
    public void testGetAllBookings() throws Exception {
        mockMvc.perform(get("/api/booking/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].purpose", is("Study Session")));
    }

    @Test
    public void testGetBookingEndTime() throws Exception {
        Long roomId = 1L;
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        Booking booking = new Booking("user123", "room123", LocalDateTime.now(), endTime, "Study Session");

        when(bookingService.getLatestBookingByRoomId(roomId)).thenReturn(booking);

        mockMvc.perform(get("/api/booking/getBookingEndTime")
                .param("roomId", String.valueOf(roomId)))
                .andExpect(status().isOk())
                .andExpect(content().string(endTime.toString()));
    }
}
