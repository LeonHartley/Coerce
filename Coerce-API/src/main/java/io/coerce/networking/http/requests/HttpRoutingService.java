package io.coerce.networking.http.requests;

import io.coerce.networking.http.responses.HttpResponse;

import java.util.function.BiConsumer;

public interface HttpRoutingService {
    void addRoute(HttpRequestType type, String pathPattern, BiConsumer<HttpRequest, HttpResponse> route);

    void processRoute(HttpRequest request, HttpResponse response);
}
