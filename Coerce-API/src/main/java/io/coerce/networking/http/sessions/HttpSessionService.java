package io.coerce.networking.http.sessions;

public interface HttpSessionService {
    long DEFAULT_SESSION_LIFE = 3600*72;

    HttpSession getSessionById(final String sessionId);

    HttpSession createSession(final String sessionId);

    default long getSessionLife() {
        return DEFAULT_SESSION_LIFE;
    }
}
