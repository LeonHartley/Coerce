package io.coerce.services.messaging.example.boot.messages.player;

import io.coerce.services.messaging.client.messages.response.MessageResponse;

public class PlayerDataResponse implements MessageResponse {
    private final String username;
    private final String email;

    public PlayerDataResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
