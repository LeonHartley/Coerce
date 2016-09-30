package io.coerce.services.messaging.server.sessions;

import io.coerce.networking.channels.NetworkChannel;

import java.util.concurrent.atomic.AtomicInteger;

public class Session {
    private final String alias;
    private final NetworkChannel networkChannel;
    private final AtomicInteger totalSentMessages;
    private final AtomicInteger totalReceivedMessages;

    public Session(String alias, NetworkChannel networkChannel) {
        this.alias = alias;
        this.networkChannel = networkChannel;
        this.totalSentMessages = new AtomicInteger(0);
        this.totalReceivedMessages = new AtomicInteger(0);
    }

    public String getAlias() {
        return alias;
    }

    public NetworkChannel getNetworkChannel() {
        return networkChannel;
    }

    public AtomicInteger getTotalSentMessages() {
        return this.totalSentMessages;
    }

    public AtomicInteger getTotalReceivedMessages() {
        return this.totalReceivedMessages;
    }
}
