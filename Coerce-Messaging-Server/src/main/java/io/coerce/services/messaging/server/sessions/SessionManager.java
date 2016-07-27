package io.coerce.services.messaging.server.sessions;

import io.coerce.networking.channels.NetworkChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final SessionManager sessionManager = new SessionManager();

    private final Map<String, Session> sessions;

    public SessionManager() {
        this.sessions = new ConcurrentHashMap<>();
    }

    public static SessionManager getInstance() {
        return sessionManager;
    }

    public Session getSession(final String alias) {
        return this.sessions.get(alias);
    }

    public void removeSession(final String alias) {
        this.sessions.remove(alias);
    }

    public Session createSession(final String alias, final NetworkChannel networkChannel) {
        final Session session = new Session(alias, networkChannel);

        this.sessions.put(alias, session);

        return session;
    }
}
