package io.coerce.services.messaging.example.boot.messages.time;

import io.coerce.services.messaging.client.messages.response.MessageResponse;

public class TimeMessageResponse implements MessageResponse {
    private final String time;

    public TimeMessageResponse(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
