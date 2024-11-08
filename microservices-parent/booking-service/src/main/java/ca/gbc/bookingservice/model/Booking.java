package ca.gbc.bookingservice.model;

import lombok.*;
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
    private String userName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;
    private String status;

}
