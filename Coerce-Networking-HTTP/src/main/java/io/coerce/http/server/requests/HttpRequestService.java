package io.coerce.http.server.requests;

import com.google.common.collect.Maps;
import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.networking.http.responses.HttpResponse;
import org.bigtesting.routd.Route;
import org.bigtesting.routd.TreeRouter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class HttpRequestService implements HttpRoutingService {
    private final Map<HttpRequestType, Map<Route, List<BiConsumer<HttpRequest, HttpResponse>>>> requestConsumers;

    private final TreeRouter treeRouter;

    public HttpRequestService() {
        this.requestConsumers = new ConcurrentHashMap<>();

        this.treeRouter = new TreeRouter();

        this.requestConsumers.put(HttpRequestType.POST, Maps.newConcurrentMap());
        this.requestConsumers.put(HttpRequestType.GET, Maps.newConcurrentMap());
    }

    @Override
    public void addRoute(HttpRequestType type, String pathPattern, BiConsumer<HttpRequest, HttpResponse> route) {
        final Route router = new Route(pathPattern);

        this.treeRouter.add(router);

        if (!this.requestConsumers.get(type).containsKey(router)) {
            this.requestConsumers.get(type).put(router, new CopyOnWriteArrayList<>());
        }

        this.requestConsumers.get(type).get(router).add(route);
    }

    @Override
    public void processRoute(HttpRequest request, HttpResponse response) {
        final Route route = this.treeRouter.route(request.getLocation());

        if (route != null) {
            ((DefaultHttpRequest) request).setRoute(route);

            if (this.requestConsumers.get(request.getType()).containsKey(route)) {
                for (BiConsumer<HttpRequest, HttpResponse> requestConsumer :
                        this.requestConsumers.get(request.getType()).get(route)) {

                    requestConsumer.accept(request, response);
                }
            }

            return;
        }

        response.setContentType("text/html");
        response.setResponseCode(404);

        response.send("404 not found");
    }
}
