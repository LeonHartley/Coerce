package io.coerce.services.messaging.server.sessions;

import io.coerce.networking.channels.NetworkChannel;

public class Session {
    private final String alias;
    private final NetworkChannel networkChannel;

    public Session(String alias, NetworkChannel networkChannel) {
        this.alias = alias;
        this.networkChannel = networkChannel;
    }

    public String getAlias() {
        return alias;
    }

    public NetworkChannel getNetworkChannel() {
        return networkChannel;
    }
}
