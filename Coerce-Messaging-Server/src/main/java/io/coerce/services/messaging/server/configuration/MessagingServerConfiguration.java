package io.coerce.services.messaging.server.configuration;

import io.coerce.services.configuration.ServiceConfiguration;

public class MessagingServerConfiguration implements ServiceConfiguration {

    private final String hostName;
    private final int port;

    public MessagingServerConfiguration(final String hostName, final int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPort() {
        return this.port;
    }
}
