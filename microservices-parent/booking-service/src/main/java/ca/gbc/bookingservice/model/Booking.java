package ca.gbc.bookingservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "booking")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    private String id;
    private Long roomId;
    private String bookHolderName; // Name of the person making the booking

    private Integer numberOfPeople; // Number of people for the booking, to check against room capacity

    private LocalDateTime bookingStart; // Start time of the booking

    private LocalDateTime bookingEnd; // End time of the booking

    private String status; // e.g., "Confirmed", "Pending", "Cancelled"

}
