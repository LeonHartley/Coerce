package io.coerce.services.messaging.server.web;

import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.networking.http.responses.HttpResponseCode;
import io.coerce.services.messaging.server.sessions.Session;
import io.coerce.services.messaging.server.sessions.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceController {
    private final SessionManager sessionManager;

    public ServiceController(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void index(HttpRequest req, HttpResponse res) {
        final Map<String, Object> model = new HashMap<>();
        final List<String> services = new ArrayList<>();

        for (String alias : this.sessionManager.getSessions().keySet()) {
            services.add(alias);
        }

        model.put("services", services);
        res.renderView("index.messaging", model);
    }

    public void status(HttpRequest request, HttpResponse response) {
        final Map<String, Object> model = new HashMap<>();
        final String serviceName = request.getUrlParameter("serviceName");

        final Session session = this.sessionManager.getSession(serviceName);

        if(session == null) {
            response.redirect("/");
            return;
        }

        model.put("serviceName", serviceName);
        model.put("totalSentMessages", session.getTotalSentMessages().get());
        model.put("totalReceivedMessages", session.getTotalReceivedMessages().get());

        response.renderView("status.messaging", model);
    }
}
