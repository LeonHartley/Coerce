package io.coerce.http.server;

import io.coerce.http.CoreHttpServerService;
import io.coerce.http.codec.HttpPayloadDecoder;
import io.coerce.http.codec.HttpPayloadEncoder;
import io.coerce.http.server.requests.HttpRequestQueue;
import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.codec.ObjectEncoder;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.responses.views.ViewParser;

public class HttpChannelHandler implements NetworkChannelHandler<HttpPayload> {
    private final ObjectEncoder<HttpPayload> encoder;
    private final ObjectDecoder<HttpPayload> decoder;

    private final CoreHttpServerService serverService;

    public HttpChannelHandler(CoreHttpServerService serverService) {
        this.serverService = serverService;

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
            final DefaultHttpRequest httpRequest = (DefaultHttpRequest) message;

            httpRequest.setNetworkChannel(networkChannel);
            httpRequest.setViewParser(this.serverService.getViewParser());
        }

        // TODO: request logging
        this.serverService.getRequestQueue().enqueue((HttpRequest) message);
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
