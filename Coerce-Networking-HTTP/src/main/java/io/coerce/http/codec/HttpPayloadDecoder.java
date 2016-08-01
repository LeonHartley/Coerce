package io.coerce.http.codec;

import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.requests.HttpRequestType;

import java.nio.charset.Charset;
import java.util.HashMap;

public class HttpPayloadDecoder implements ObjectDecoder<HttpPayload> {
    @Override
    public HttpPayload decode(NetworkBuffer buffer, NetworkChannel channel) {
        final String requestData = buffer.toString(Charset.defaultCharset());
        final String[] httpRequestTypeParts = requestData.split("\r\n")[0].split(" ");

        final HttpRequestType type = HttpRequestType.valueOf(httpRequestTypeParts[0]);
        final String location = httpRequestTypeParts[1];
        final String httpVersion = httpRequestTypeParts[2];

        return new DefaultHttpRequest(type, location, httpVersion, new HashMap<String, String>());
    }
}
