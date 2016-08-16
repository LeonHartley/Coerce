package io.coerce.networking.http.sessions;

public interface HttpSession {
    <T> T getObject(SessionObjectKey<T> key);

    <T> void setObject(SessionObjectKey<T> key, T object);
}
