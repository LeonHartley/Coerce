package io.coerce.services.messaging.example.boot;

import com.google.common.base.Stopwatch;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.config.Configuration;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.MessageResponse;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataRequest;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataResponse;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MessagingApplication {

    private final MessagingClient messagingClient;

    public MessagingApplication(final String[] args) throws Exception {

       // final Stopwatch stopwatch = Stopwatch.createStarted();

        final CoerceConfiguration configuration = new CoerceConfiguration();

        this.messagingClient = MessagingClient.create("test-client", configuration);

        this.messagingClient.connect("localhost", 8080, (client) -> {
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(2));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(3));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(1));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(2));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(3));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(1));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(2));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(3));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(1));
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(2));
        });
    }

    public static void main(String[] args) throws Exception {
        final MessagingApplication application = new MessagingApplication(args);
    }
}
