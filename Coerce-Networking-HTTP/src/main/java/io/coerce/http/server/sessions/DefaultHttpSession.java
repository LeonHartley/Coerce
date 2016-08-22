package io.coerce.http.server.sessions;

import io.coerce.networking.http.sessions.HttpSession;
import io.coerce.networking.http.sessions.SessionObjectKey;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultHttpSession implements HttpSession {
    private final Map<String, Object> sessionObjects;

    public DefaultHttpSession() {
        this.sessionObjects = new ConcurrentHashMap<>();
    }

    @Override
    public <T> T getObject(SessionObjectKey<T> key) {
        if(this.sessionObjects.containsKey(key.getName())) {
            return (T) this.sessionObjects.get(key.getName());
        }

        return null;
    }

    @Override
    public <T> void setObject(SessionObjectKey<T> key, T object) {
        this.sessionObjects.put(key.getName(), object);
    }
}
