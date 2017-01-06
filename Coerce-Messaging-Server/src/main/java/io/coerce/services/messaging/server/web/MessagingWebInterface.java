package io.coerce.services.messaging.server.web;

import com.google.gson.Gson;
import io.coerce.networking.http.HttpServerService;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.services.messaging.server.sessions.SessionManager;

import java.util.HashMap;
import java.util.Map;

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
        this.routingService.routeStatic("/", "static");

//        this.routingService.addRoute(HttpRequestType.GET, "/", this.serviceController::index);
//        this.routingService.addRoute(HttpRequestType.GET, "/service/:serviceName", this.serviceController::status);
//
//        this.routingService.addRoute(HttpRequestType.GET, "/form", (req, res) -> res.renderView("form", new HashMap<>()));
//
//        this.routingService.addRoute(HttpRequestType.POST, "/form", (req, res) -> {
//            final Map<String, Object> model = new HashMap<>();
//
//            model.put("formData", new Gson().toJson(req.getFormData()));
//            res.renderView("form", model);
//        });
    }
}
