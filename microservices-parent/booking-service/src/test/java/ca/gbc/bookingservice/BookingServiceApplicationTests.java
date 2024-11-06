package ca.gbc.bookingservice;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.stub.RoomClientStub;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceApplicationTests {

    @LocalServerPort
    private Integer port;

    @Autowired
    private RoomClient roomClient;

    @BeforeEach
    void setup() {
        // Set up WireMock for stubbing RoomClient requests
        WireMock.configureFor("localhost", 8089); // Ensure WireMock is running on a different port, here 8089
        RoomClientStub.stubRoomAvailability("roomA", "2024-10-30T10:00:00", "2024-10-30T12:00:00");

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    // Helper method to create a booking for test purposes
    private void createBooking(String userId, String roomId, String startTime, String endTime, String purpose) {
        String requestBody = String.format("""
                {
                    "userId": "%s",
                    "roomId": "%s",
                    "startTime": "%s",
                    "endTime": "%s",
                    "purpose": "%s"
                }
                """, userId, roomId, startTime, endTime, purpose);

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/booking")
                .then()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("userId", Matchers.equalTo(userId))
                .body("roomId", Matchers.equalTo(roomId))
                .body("startTime", Matchers.equalTo(startTime))
                .body("endTime", Matchers.equalTo(endTime))
                .body("purpose", Matchers.equalTo(purpose));
    }

    @Test
    void createBookingTest() {
        createBooking("user123", "roomA", "2024-10-30T10:00:00", "2024-10-30T12:00:00", "Meeting");
    }

    @Test
    void getAllBookingsTest() {
        createBooking("user123", "roomA", "2024-10-30T10:00:00", "2024-10-30T12:00:00", "Meeting");

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].userId", Matchers.equalTo("user123"))
                .body("[0].roomId", Matchers.equalTo("roomA"))
                .body("[0].startTime", Matchers.equalTo("2024-10-30T10:00:00"))
                .body("[0].endTime", Matchers.equalTo("2024-10-30T12:00:00"))
                .body("[0].purpose", Matchers.equalTo("Meeting"));
    }

    @Test
    void getBookingByIdTest() {
        createBooking("user123", "roomA", "2024-10-30T10:00:00", "2024-10-30T12:00:00", "Meeting");

        String bookingId = RestAssured.given()
                .contentType("application/json")
                .body("""
                        {
                            "userId": "user123",
                            "roomId": "roomA",
                            "startTime": "2024-10-30T10:00:00",
                            "endTime": "2024-10-30T12:00:00",
                            "purpose": "Meeting"
                        }
                        """)
                .post("/api/booking")
                .then()
                .extract().path("id");

        RestAssured.given()
                .when()
                .get("/api/booking/" + bookingId)
                .then()
                .statusCode(200)
                .body("id", Matchers.equalTo(bookingId))
                .body("userId", Matchers.equalTo("user123"))
                .body("roomId", Matchers.equalTo("roomA"))
                .body("startTime", Matchers.equalTo("2024-10-30T10:00:00"))
                .body("endTime", Matchers.equalTo("2024-10-30T12:00:00"))
                .body("purpose", Matchers.equalTo("Meeting"));
    }

    @Test
    void deleteBookingTest() {
        createBooking("user123", "roomA", "2024-10-30T10:00:00", "2024-10-30T12:00:00", "Meeting");

        String bookingId = RestAssured.given()
                .contentType("application/json")
                .body("""
                        {
                            "userId": "user123",
                            "roomId": "roomA",
                            "startTime": "2024-10-30T10:00:00",
                            "endTime": "2024-10-30T12:00:00",
                            "purpose": "Meeting"
                        }
                        """)
                .post("/api/booking")
                .then()
                .extract().path("id");

        RestAssured.given()
                .when()
                .delete("/api/booking/" + bookingId)
                .then()
                .statusCode(204);

        // Verify deletion
        RestAssured.given()
                .when()
                .get("/api/booking/" + bookingId)
                .then()
                .statusCode(404);
    }

    @Test
    void createBookingWithMissingFields_shouldReturnBadRequest() {
        String invalidRequestBody = """
                {
                    "userId": "user123",
                    "startTime": "2024-10-30T10:00:00",
                    "purpose": "Meeting"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(invalidRequestBody)
                .when()
                .post("/api/booking")
                .then()
                .statusCode(400)
                .body("error", Matchers.equalTo("Bad Request"));
    }
}
