package io.coerce.networking;

import io.coerce.networking.channels.NetworkChannelHandler;

public interface NetworkingService {
    void initialise(final NetworkChannelHandler channelHandler);

    void startService(final String host, final int port);
}
