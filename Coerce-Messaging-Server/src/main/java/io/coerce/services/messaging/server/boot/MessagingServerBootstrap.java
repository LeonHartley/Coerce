package io.coerce.services.messaging.server.boot;

import io.coerce.services.ServiceBootstrap;
import io.coerce.services.messaging.server.MessagingServer;

public class MessagingServerBootstrap {
    public static void main(final String[] args) {
        final MessagingServer service = ServiceBootstrap.startService(MessagingServer.class, args);

        // Anything we wanna do with the service once it's started?
    }
}
