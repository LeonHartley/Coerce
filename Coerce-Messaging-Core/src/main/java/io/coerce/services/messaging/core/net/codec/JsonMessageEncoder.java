package io.coerce.services.messaging.core.net.codec;

import com.google.gson.Gson;
import io.coerce.messaging.types.StringMessage;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectEncoder;

public class JsonMessageEncoder implements ObjectEncoder<StringMessage> {
    private static final Gson gson = new Gson();

    @Override
    public NetworkBuffer encode(StringMessage object, NetworkChannel channel, NetworkBuffer out) {
        final byte[] data = gson.toJson(object).getBytes();

        out.writeInteger(data.length);
        out.writeBytes(data);

        return out;
    }

    @Override
    public boolean hasDelimiter() {
        return true;
    }

    @Override
    public byte[] getDelimiter() {
        return new byte[] { '\n' };
    }
}