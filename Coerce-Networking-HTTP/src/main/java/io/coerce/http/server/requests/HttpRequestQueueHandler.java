package io.coerce.http.server.requests;

import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.http.types.DefaultHttpResponse;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.networking.http.sessions.HttpSession;

import java.util.UUID;

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
                final DefaultHttpRequest httpRequest = (DefaultHttpRequest) this.requestQueue.getQueue().poll();

                if(httpRequest == null) {
                    continue;
                }

                final Cookie sessionCookie = httpRequest.getCookies().get("COERCE_SESSION");

                final HttpResponse httpResponse = new DefaultHttpResponse(
                        httpRequest.getViewParser(),
                        httpRequest.getNetworkChannel());

                if(sessionCookie == null) {
                    final String sessionId = "COERCE-" + UUID.randomUUID();

                    // create a session, assign it via this cookie
                    httpResponse.setCookie(new Cookie("COERCE_SESSION", sessionId, "2038-01-19 04:14:07"));
                    httpRequest.setSession(this.requestQueue.getSessionService().createSession(sessionId));
                } else {
                    final HttpSession session = this.requestQueue.getSessionService().getSessionById(sessionCookie.getValue());

                    if(session == null) {
                        httpRequest.setSession(this.requestQueue.getSessionService().createSession(sessionCookie.getValue()));
                    } else {
                        httpRequest.setSession(session);
                    }
                }

                this.requestQueue.getRoutingService().processRoute(httpRequest, httpResponse);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
