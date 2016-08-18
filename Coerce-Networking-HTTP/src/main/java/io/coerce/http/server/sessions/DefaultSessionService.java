package io.coerce.http.server.sessions;

import io.coerce.networking.http.sessions.HttpSession;
import io.coerce.networking.http.sessions.HttpSessionService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is just an example session service. This should certainly not be used in production.
 */
public class DefaultSessionService implements HttpSessionService {

    private final Map<String, HttpSession> sessions;

    public DefaultSessionService() {
        this.sessions = new ConcurrentHashMap<>();
    }

    @Override
    public HttpSession getSessionById(String sessionId) {
        if(this.sessions.containsKey(sessionId)) {
            return this.sessions.get(sessionId);
        }

        return null;
    }

    @Override
    public HttpSession createSession(String sessionId) {
        final DefaultHttpSession httpSession = new DefaultHttpSession();

        this.sessions.put(sessionId, httpSession);
        return httpSession;
    }
}
