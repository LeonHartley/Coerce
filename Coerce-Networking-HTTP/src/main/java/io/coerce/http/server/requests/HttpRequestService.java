package io.coerce.http.server.requests;

import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.networking.http.responses.HttpResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class HttpRequestService implements HttpRoutingService {
    private final Map<String, List<BiConsumer<HttpRequest, HttpResponse>>> requestConsumers;

    public HttpRequestService() {
        this.requestConsumers = new ConcurrentHashMap<>();
    }

    @Override
    public void addRoute(HttpRequestType type, String pathPattern, BiConsumer<HttpRequest, HttpResponse> route) {
        if(!this.requestConsumers.containsKey(pathPattern)) {
            this.requestConsumers.put(pathPattern, new CopyOnWriteArrayList<>());
        }

        this.requestConsumers.get(pathPattern).add(route);
    }

    @Override
    public void processRoute(HttpRequest request, HttpResponse response) {
        // TODO: URL PATTERNS (/user/:id) etc.

        if(this.requestConsumers.containsKey(request.getLocation())) {
            for(BiConsumer<HttpRequest, HttpResponse> requestConsumer :
                    this.requestConsumers.get(request.getLocation())) {

                requestConsumer.accept(request, response);
            }
        }
    }
}
