package io.coerce.services.messaging.server.web;

import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;

public class MessagingWebInterface {
    private final HttpRoutingService routingService;
    private final StatusController statusController;

    public MessagingWebInterface(HttpRoutingService routingService) {
        this.routingService = routingService;

        // initialise controllers
        this.statusController = new StatusController();
    }

    public void initialiseRoutes() {
        this.routingService.addRoute(HttpRequestType.GET, "/status", this.statusController::index);
        this.routingService.addRoute(HttpRequestType.GET, "/status/get", this.statusController::status);
    }
}
