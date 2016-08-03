package io.coerce.http.codec;

import com.google.common.primitives.Bytes;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectEncoder;
import io.coerce.networking.http.HttpPayload;

import java.util.Map;

public class HttpPayloadEncoder implements ObjectEncoder<HttpPayload> {
    @Override
    public NetworkBuffer encode(HttpPayload object, NetworkChannel channel, NetworkBuffer out) {
        final StringBuilder httpResponseBuilder = new StringBuilder();

        httpResponseBuilder.append(object.getHeader() + "\r\n");

        for (Map.Entry<String, String> header : object.getHeaders().entrySet()) {
            httpResponseBuilder.append(header.getKey() + ": " + header.getValue() + "\r\n");
        }

        httpResponseBuilder.append("\r\n");

        out.writeBytes(Bytes.concat(
                httpResponseBuilder.toString().getBytes(),
                object.getData()));

        return out;
    }
}