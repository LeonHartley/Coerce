package io.coerce.networking.http;

import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.networking.http.sessions.HttpSessionService;

public interface HttpServerService {
    void startServer(final String host, final int port);

    HttpRoutingService getRoutingService();

    void setRoutingService(final HttpRoutingService routingService);

    void setSessionService(final HttpSessionService sessionService);

}