package io.coerce.services.messaging.core.net.codec;

import com.google.gson.Gson;
import io.coerce.messaging.types.StringMessage;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectEncoder;
import org.apache.commons.lang.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class MessageObjectEncoder implements ObjectEncoder<StringMessage> {

    @Override
    public NetworkBuffer encode(StringMessage object, NetworkChannel channel, NetworkBuffer out) {
        byte[] data = null;

        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutput objectOutputStream = new ObjectOutputStream(byteStream);

            objectOutputStream.writeObject(object);
            objectOutputStream.close();

            data = byteStream.toByteArray();

            byteStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data == null) {
            out.writeInteger(0);
            return out;
        }

        System.out.println(data.length);

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
        return new byte[] { '\n', '|', '\n' };
    }
}
