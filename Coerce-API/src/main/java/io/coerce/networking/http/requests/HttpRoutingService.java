package io.coerce.networking.http.requests;

import io.coerce.networking.http.files.FileProvider;
import io.coerce.networking.http.responses.HttpResponse;

import java.util.function.BiConsumer;

public interface HttpRoutingService {
    void addRoute(HttpRequestType type, String pathPattern, BiConsumer<HttpRequest, HttpResponse> route);

    void routeStatic(final String route, final String staticDirectory);

    void processRoute(HttpRequest request, HttpResponse response);

    FileProvider getFileProvider();

    //void setErrorRoute(HttpErrorType type, BiConsumer<HttpRequest, HttpResponse> route);
}
