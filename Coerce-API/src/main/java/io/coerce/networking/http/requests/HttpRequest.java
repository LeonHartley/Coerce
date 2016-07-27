package io.coerce.networking.http.requests;

import io.coerce.networking.http.sessions.HttpSession;

public interface HttpRequest {
    HttpRequestType getType();

    HttpSession getSession();
}
