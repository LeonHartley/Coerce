package io.coerce.services.messaging.server.sessions;

import com.google.common.collect.Lists;
import io.coerce.messaging.Message;
import io.coerce.networking.channels.NetworkChannel;

import java.util.List;
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

    public void broadcast(final Message message) {
        this.sessions.forEach((alias, session) -> {
            if (session.getNetworkChannel() != null) {
                session.getNetworkChannel().writeAndFlush(message);
            }
        });
    }

    public List<Session> getSessionsByAliasPattern(final String aliasPattern) {
        final List<Session> sessions = Lists.newArrayList();

        for(Session session : this.sessions.values()) {
            if(session.getAlias().startsWith(aliasPattern)) {
                sessions.add(session);
            }
        }

        return sessions;
    }

    public Session createSession(final String alias, final NetworkChannel networkChannel) {
        final Session session = new Session(alias, networkChannel);

        this.sessions.put(alias, session);

        return session;
    }

    public Map<String, Session> getSessions() {
        return this.sessions;
    }
}
