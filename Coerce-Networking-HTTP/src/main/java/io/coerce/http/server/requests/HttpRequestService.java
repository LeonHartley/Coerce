package io.coerce.http.server.requests;

import com.google.common.collect.Maps;
import io.coerce.http.files.DefaultCachedFileProvider;
import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.http.files.FileData;
import io.coerce.networking.http.files.FileProvider;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.requests.HttpRoutingService;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.networking.http.responses.HttpResponseCode;
import org.bigtesting.routd.Route;
import org.bigtesting.routd.TreeRouter;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class HttpRequestService implements HttpRoutingService {
    private final Map<HttpRequestType, Map<Route, List<BiConsumer<HttpRequest, HttpResponse>>>> requestConsumers;

    private FileProvider fileProvider;
    private final TreeRouter treeRouter;

    public HttpRequestService() {
        this.requestConsumers = new ConcurrentHashMap<>();

        this.treeRouter = new TreeRouter();
        this.fileProvider = new DefaultCachedFileProvider();

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
    public void routeStatic(String route, String staticDirectory) {
        final BiConsumer<HttpRequest, HttpResponse> consumer = (req, res) -> {
            String request = req.getUrlParameter("*").replace("../", "");

            if(request.isEmpty()) {
                request = "index.html";
            }

            final FileData data = fileProvider.getFile(staticDirectory + "/" + request);

            if(data == null) {
                this.notFound(res);
                return;
            }

            res.setContentType(data.getFileType().getContentType());
            res.setResponseCode(HttpResponseCode.OK);
            res.send(data.getData());
        };

        this.addRoute(HttpRequestType.GET, route.equals("/") ? "*" : route + "/*", consumer);
        this.addRoute(HttpRequestType.POST, route.equals("/") ? "*" : route  + "/*", consumer);
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
    }

    private void notFound(HttpResponse response) {
        response.setContentType("text/html");
        response.setResponseCode(HttpResponseCode.NOT_FOUND);

        response.send("404 not found");
    }

    @Override
    public FileProvider getFileProvider() {
        return this.fileProvider;
    }
}
