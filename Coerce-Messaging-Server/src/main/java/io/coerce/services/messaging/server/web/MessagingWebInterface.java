package io.coerce.services.messaging.server.web;

import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.services.messaging.server.sessions.SessionManager;

public class MessagingWebInterface {
    private final HttpRoutingService routingService;
    private final SessionManager sessionManager;

    private final ServiceController serviceController;

    public MessagingWebInterface(HttpRoutingService routingService, SessionManager sessionManager) {
        this.routingService = routingService;
        this.sessionManager = sessionManager;

        // initialise controllers
        this.serviceController = new ServiceController(sessionManager);
    }

    public void initialiseRoutes() {
        this.routingService.addRoute(HttpRequestType.GET, "/", this.serviceController::index);
        this.routingService.addRoute(HttpRequestType.GET, "/service/:serviceName", this.serviceController::status);
    }
}
