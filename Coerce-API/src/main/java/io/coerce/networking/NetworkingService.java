package io.coerce.networking;

import io.coerce.networking.channels.NetworkChannelHandler;

import java.util.function.Consumer;

public interface NetworkingService {
    void initialise(final NetworkChannelHandler channelHandler);

    void startService(String host, int port, Consumer<NetworkingService> onServiceStarted);
}
