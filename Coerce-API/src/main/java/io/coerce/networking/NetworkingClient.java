package io.coerce.networking;

import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;

import java.util.function.Consumer;

public interface NetworkingClient {
    void configure(NetworkChannelHandler handler);

    void connect(String host, int port, boolean autoReconnect, Consumer<NetworkChannel> onConnect);
}
