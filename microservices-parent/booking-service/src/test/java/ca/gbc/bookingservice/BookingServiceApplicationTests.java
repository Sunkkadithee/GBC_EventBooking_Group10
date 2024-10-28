package ca.gbc.bookingservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.util.PathMatcher;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceApplicationTests {

    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @LocalServerPort
    private Integer port;

    @Autowired
    private PathMatcher mvcPathMatcher;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        mongoDBContainer.start();
    }

    @Test
    void createBookingTest() {
        String requestBody = """
                {
                    "userId": "user123",
                    "roomId": "roomA",
                    "startTime": "2024-10-30T10:00:00",
                    "endTime": "2024-10-30T12:00:00",
                    "purpose": "Meeting"
                }
                """;

        // BDD - Behaviour Driven Development
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/booking")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", Matchers.notNullValue())
                .body("userId", Matchers.equalTo("user123"))
                .body("roomId", Matchers.equalTo("roomA"))
                .body("startTime", Matchers.equalTo("2024-10-30T10:00:00"))
                .body("endTime", Matchers.equalTo("2024-10-30T12:00:00"))
                .body("purpose", Matchers.equalTo("Meeting"));
    }

    @Test
    void getAllBookingsTest() {
        String requestBody = """
                {
                    "userId": "user123",
                    "roomId": "roomA",
                    "startTime": "2024-10-30T10:00:00",
                    "endTime": "2024-10-30T12:00:00",
                    "purpose": "Meeting"
                }
                """;

        // Create a booking first to ensure there's at least one to retrieve
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/booking")
                .then()
                .log().all()
                .statusCode(201)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].userId", Matchers.equalTo("user123"))
                .body("[0].roomId", Matchers.equalTo("roomA"))
                .body("[0].startTime", Matchers.equalTo("2024-10-30T10:00:00"))
                .body("[0].endTime", Matchers.equalTo("2024-10-30T12:00:00"))
                .body("[0].purpose", Matchers.equalTo("Meeting"));


        // Now, retrieve all bookings
        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0)) // Check that at least one booking exists
                .body("[0].userId", Matchers.equalTo("user123"))
                .body("[0].roomId", Matchers.equalTo("roomA"))
                .body("[0].startTime", Matchers.equalTo("2024-10-30T10:00:00"))
                .body("[0].endTime", Matchers.equalTo("2024-10-30T12:00:00"))
                .body("[0].purpose", Matchers.equalTo("Meeting"));
    }
}