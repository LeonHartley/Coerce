package io.coerce.networking.http.sessions;

public class SessionObjectKey<T> {
    private final String name;

    public SessionObjectKey(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public static SessionObjectKey forName(final String name) {
        return new SessionObjectKey(name);
    }
}
