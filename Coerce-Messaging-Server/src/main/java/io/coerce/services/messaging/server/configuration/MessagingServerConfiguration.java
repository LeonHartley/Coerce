package io.coerce.services.messaging.server.configuration;

import io.coerce.services.configuration.ServiceConfiguration;

public class MessagingServerConfiguration implements ServiceConfiguration {

    private final String hostName;
    private final int port;
    private final String authenticationKey;

    public MessagingServerConfiguration(final String hostName, final int port, final String authenticationKey) {
        this.hostName = hostName;
        this.port = port;
        this.authenticationKey = authenticationKey;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPort() {
        return this.port;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }
}
