package io.coerce.services.messaging.server.web;

import com.google.common.collect.Maps;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.responses.HttpResponse;

public class StatusController {
    public void index(HttpRequest request, HttpResponse response) {
        response.renderView("index.messaging", Maps.newConcurrentMap());
    }

    public void status(HttpRequest request, HttpResponse response) {
        response.renderView("status.messaging", Maps.newConcurrentMap());
    }
}
