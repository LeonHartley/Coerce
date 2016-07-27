package io.coerce.services.messaging.core.net.codec;

import com.google.gson.Gson;
import io.coerce.messaging.types.ObjectMessage;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;

import java.io.UnsupportedEncodingException;

public class JsonMessageDecoder implements ObjectDecoder<ObjectMessage> {
    private static final Gson gson = new Gson();

    @Override
    public ObjectMessage decode(NetworkBuffer buffer, NetworkChannel channel) {
        if(buffer.readableBytes() < 4) {
            return null;
        }

        buffer.markReaderIndex();

        final int length = buffer.readInteger();

        if(length < 0 || buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            return null;
        }

        final byte[] bytes = buffer.readBytes(length);

        try {
            return gson.fromJson(new String(bytes, "UTF-8"), ObjectMessage.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
