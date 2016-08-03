package io.coerce.http.codec;

import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.requests.HttpRequestType;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpPayloadDecoder implements ObjectDecoder<HttpPayload> {
    @Override
    public HttpPayload decode(NetworkBuffer buffer, NetworkChannel channel) {
        final String requestData = buffer.toString(Charset.defaultCharset());
        final String[] requestLines = requestData.split("\r\n");

        final String[] httpRequestTypeParts = requestLines[0].split(" ");

        final HttpRequestType type = HttpRequestType.valueOf(httpRequestTypeParts[0]);
        final String location = httpRequestTypeParts[1];
        final String httpVersion = httpRequestTypeParts[2];

        final Map<String, String> headers = new HashMap<>();

        for(int i = 1; i < requestLines.length; i++) {
            final String[] header = requestLines[i].split(":");
            headers.put(header[0].trim(), header[1].trim());
        }

        return new DefaultHttpRequest(type, location, httpVersion, headers);
    }
}
