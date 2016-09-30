package io.coerce.services.messaging.server.web;

import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;

public class MessagingWebInterface {
    private final HttpRoutingService routingService;
    private final ServiceController serviceController;

    public MessagingWebInterface(HttpRoutingService routingService) {
        this.routingService = routingService;

        // initialise controllers
        this.serviceController = new ServiceController();
    }

    public void initialiseRoutes() {
        this.routingService.addRoute(HttpRequestType.GET, "/", this.serviceController::index);
        this.routingService.addRoute(HttpRequestType.GET, "/service/:serviceName", this.serviceController::status);
    }
}
