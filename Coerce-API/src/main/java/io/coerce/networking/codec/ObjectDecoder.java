package io.coerce.networking.codec;

import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;

public interface ObjectDecoder<T> {
    T decode(NetworkBuffer buffer, NetworkChannel channel);
}
