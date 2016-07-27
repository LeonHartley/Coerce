package io.coerce.networking.codec.strings;

import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectEncoder;

public class StringEncoder implements ObjectEncoder<String> {
    @Override
    public NetworkBuffer encode(String object, NetworkChannel channel, NetworkBuffer out) {
        out.writeString(object);

        return out;
    }
}
