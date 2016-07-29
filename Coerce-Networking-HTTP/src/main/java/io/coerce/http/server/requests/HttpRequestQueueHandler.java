package io.coerce.http.server.requests;

import io.coerce.networking.http.requests.HttpRequest;

public class HttpRequestQueueHandler extends Thread {

    private final HttpRequestQueue requestQueue;
    private volatile boolean running = true;

    public HttpRequestQueueHandler(final int handlerId, final HttpRequestQueue queue) {
        this.setName("HttpRequestQueue-" + handlerId);

        this.requestQueue = queue;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                // Poll the queue for entries and handle them.
                final HttpRequest httpRequest = this.requestQueue.getQueue().poll();

                if(httpRequest == null) {
                    continue;
                }

                this.requestQueue.getRoutingService().processRoute(httpRequest, null);
                //System.out.println("[" + this.getName() + "] " + httpRequest.getLocation());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
