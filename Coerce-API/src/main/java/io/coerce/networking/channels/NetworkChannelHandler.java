package io.coerce.networking.channels;

import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.codec.ObjectEncoder;

public interface NetworkChannelHandler<T> {
    void onChannelActive(NetworkChannel networkChannel);

    void onChannelInactive(NetworkChannel networkChannel);

    void onChannelError(Throwable error, NetworkChannel networkChannel);

    void onMessageReceived(T message, NetworkChannel networkChannel);

    ObjectEncoder<T> getObjectEncoder();

    ObjectDecoder<T> getObjectDecoder();

    NetworkChannel getLastChannel();
}
