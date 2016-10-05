package io.coerce.networking.http;

import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.networking.http.responses.views.ViewParser;
import io.coerce.networking.http.sessions.HttpSessionService;

import java.util.function.Consumer;

public interface HttpServerService {
    void startServer(final String host, final int port, Consumer<HttpServerService> onCompletion);

    HttpRoutingService getRoutingService();

    void setRoutingService(final HttpRoutingService routingService);

    HttpSessionService getSessionService();

    void setSessionService(final HttpSessionService sessionService);

    void setViewParser(final ViewParser viewParser);

    ViewParser getViewParser();
}