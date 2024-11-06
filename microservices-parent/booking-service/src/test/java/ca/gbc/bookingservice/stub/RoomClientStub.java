package ca.gbc.bookingservice.stub;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

public class RoomClientStub {

    public static void stubRoomAvailability(String roomId, String startTime, String endTime) {
        stubFor(get(urlPathEqualTo("/api/room/check-availability"))
                .withQueryParam("roomId", equalTo(roomId))
                .withQueryParam("startTime", equalTo(startTime))
                .withQueryParam("endTime", equalTo(endTime))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true"))
        );
    }
}
