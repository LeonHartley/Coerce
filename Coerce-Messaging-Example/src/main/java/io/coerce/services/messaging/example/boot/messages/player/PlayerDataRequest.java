package io.coerce.services.messaging.example.boot.messages.player;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.UUID;

public class PlayerDataRequest extends MessageRequest<PlayerDataResponse> {
    private final int playerId;

    public PlayerDataRequest(final int playerId) {
        super(UUID.randomUUID(), PlayerDataResponse.class);

        this.playerId = playerId;
    }

    @Override
    protected void onResponseReceived(PlayerDataResponse response) {
        System.out.println("Received user with username: " + response.getUsername());
    }

    public int getPlayerId() {
        return playerId;
    }
}
