package io.coerce.services.messaging.example.boot;

import com.google.common.collect.Maps;
import io.coerce.commons.config.Configuration;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataRequest;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataResponse;

import java.util.Map;

public class PlayerDataService {
    private static final Map<Integer, String> database = Maps.newConcurrentMap();

    public static void main(String[] args) {
        database.put(1, "Leon");
        database.put(2, "Jack");
        database.put(3, "Tony");

        final Configuration configuration = new Configuration();

        final MessagingClient messagingClient = MessagingClient.create("player-service-1", configuration);

        messagingClient.observe(PlayerDataRequest.class, (playerDataRequest -> {
            messagingClient.sendResponse(playerDataRequest.getMessageId(), playerDataRequest.getSender(),
                    new PlayerDataResponse(database.get(playerDataRequest.getPlayerId()), ""));
        }));

        messagingClient.connect("localhost", 8080, (client) -> {

        });
    }
}