package io.coerce.http;

import com.google.inject.Inject;
import io.coerce.http.server.HttpChannelHandler;
import io.coerce.http.server.requests.HttpRequestQueue;
import io.coerce.http.server.requests.HttpRequestService;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.http.HttpServerService;
import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.networking.http.sessions.HttpSessionService;

public class CoreHttpServerService implements HttpServerService {

    private final NetworkingService networkingService;

    private HttpRoutingService routingService;
    private HttpSessionService sessionService;
    private NetworkChannelHandler channelHandler;

    @Inject
    public CoreHttpServerService(NetworkingService networkingService) {
        this.networkingService = networkingService;
        this.routingService = new HttpRequestService();
    }

    @Override
    public void startServer(String host, int port) {
        final HttpRequestQueue requestQueue = new HttpRequestQueue(this.getRoutingService(), 1000);

        this.networkingService.configure(new HttpChannelHandler(requestQueue));

        requestQueue.initialise(4);

        this.networkingService.startService(host, port);
    }

    @Override
    public HttpRoutingService getRoutingService() {
        return this.routingService;
    }

    @Override
    public void setRoutingService(HttpRoutingService requestService) {
        this.routingService = requestService;
    }

    @Override
    public void setSessionService(HttpSessionService sessionService) {
        this.sessionService = sessionService;
    }
}