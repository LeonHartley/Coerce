package io.coerce.http.server.requests;

import com.google.inject.Inject;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRoutingService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class HttpRequestQueue {
    private final HttpRoutingService routingService;
    private final BlockingQueue<HttpRequest> requestQueue;

    private final List<HttpRequestQueueHandler> requestHandlers;

    @Inject
    public HttpRequestQueue(HttpRoutingService routingService, int queueCapacity) {
        this.routingService = routingService;
        this.requestQueue = new ArrayBlockingQueue<>(queueCapacity);
        this.requestHandlers = new ArrayList<>();
    }

    public void initialise(final int threads) {
        for(int i = 0; i < threads; i++) {
            final HttpRequestQueueHandler handler = new HttpRequestQueueHandler(i, this);
            this.requestHandlers.add(handler);

            handler.start();
        }
    }

    public void enqueue(final HttpRequest request) {
        if(!this.requestQueue.offer(request)) {
            // TODO: backlog requests.
        }
    }

    public BlockingQueue<HttpRequest> getQueue() {
        return this.requestQueue;
    }

    public HttpRoutingService getRoutingService() {
        return this.routingService;
    }
}
