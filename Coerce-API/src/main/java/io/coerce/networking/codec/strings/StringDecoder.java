package io.coerce.networking.codec.strings;

import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;

import java.nio.charset.Charset;

public class StringDecoder implements ObjectDecoder<String> {
    @Override
    public String decode(NetworkBuffer buffer, NetworkChannel channel) {
        return buffer.toString(Charset.defaultCharset());
    }
}
