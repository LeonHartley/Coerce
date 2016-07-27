package io.coerce.services.messaging.example.boot;

import io.coerce.commons.config.Configuration;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataRequest;

public class MessagingApplication {

    private final MessagingClient messagingClient;

    public MessagingApplication(final String[] args) {
        final Configuration configuration = new Configuration();

        this.messagingClient = MessagingClient.create("test-client", configuration);

        this.messagingClient.connect("localhost", 8080, (client) -> {
            this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(1));
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

    public static void main(String[] args) {
        final MessagingApplication application = new MessagingApplication(args);
    }
}
