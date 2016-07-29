package io.coerce.networking.codec;

import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;

public interface ObjectEncoder<T> {
    NetworkBuffer encode(T object, NetworkChannel channel, NetworkBuffer out);

    default boolean hasDelimiter() {
        return false;
    }

    default byte[] getDelimiter() {
        return null;
    }
}
