package io.coerce.networking.http;

import io.coerce.networking.http.requests.HttpRequestService;
import io.coerce.networking.http.sessions.HttpSessionService;

public interface HttpServerService {
    void startServer(final String host, final int port);

    void setSessionService(final HttpSessionService sessionService);

    void setRequestService(final HttpRequestService requestService);
}
