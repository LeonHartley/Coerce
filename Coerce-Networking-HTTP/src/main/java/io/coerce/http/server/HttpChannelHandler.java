package io.coerce.http.server;

import io.coerce.http.codec.HttpPayloadDecoder;
import io.coerce.http.codec.HttpPayloadEncoder;
import io.coerce.http.server.requests.HttpRequestQueue;
import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.codec.ObjectEncoder;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.requests.HttpRequest;

public class HttpChannelHandler implements NetworkChannelHandler<HttpPayload> {

    private final ObjectEncoder<HttpPayload> encoder;
    private final ObjectDecoder<HttpPayload> decoder;

    private final HttpRequestQueue requestQueue;

    public HttpChannelHandler(HttpRequestQueue queue) {
        this.requestQueue = queue;

        this.decoder = new HttpPayloadDecoder();
        this.encoder = new HttpPayloadEncoder();
    }

    @Override
    public void onChannelActive(NetworkChannel networkChannel) {

    }

    @Override
    public void onChannelInactive(NetworkChannel networkChannel) {

    }

    @Override
    public void onChannelError(Throwable error, NetworkChannel networkChannel) {
        error.printStackTrace();
    }

    @Override
    public void onMessageReceived(HttpPayload message, NetworkChannel networkChannel) {
        if(message instanceof DefaultHttpRequest) {
            ((DefaultHttpRequest) message).setNetworkChannel(networkChannel);
        }

        // TODO: request logging
        this.requestQueue.enqueue((HttpRequest) message);
    }

    @Override
    public ObjectEncoder<HttpPayload> getObjectEncoder() {
        return this.encoder;
    }

    @Override
    public ObjectDecoder<HttpPayload> getObjectDecoder() {
        return this.decoder;
    }

    @Override
    public NetworkChannel getLastChannel() {
        return null;
    }
}
