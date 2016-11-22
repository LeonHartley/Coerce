package io.coerce.services.messaging.core.net.codec;

import com.google.gson.Gson;
import io.coerce.messaging.types.StringMessage;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class MessageObjectDecoder implements ObjectDecoder<StringMessage> {

    @Override
    public StringMessage decode(NetworkBuffer buffer, NetworkChannel channel) {
        if (buffer.readableBytes() < 4) {
            return null;
        }

        buffer.markReaderIndex();

        final int length = buffer.readInteger();

        if (length <= 0 || buffer.readableBytes() < length) {
            buffer.resetReaderIndex();
            return null;
        }

        final byte[] bytes = buffer.readBytes(length);

        StringMessage message = null;

        try {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream = new ObjectInputStream(byteArrayInputStream);

            message = (StringMessage) inputStream.readObject();

            inputStream.close();
            byteArrayInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return message;
    }
}
