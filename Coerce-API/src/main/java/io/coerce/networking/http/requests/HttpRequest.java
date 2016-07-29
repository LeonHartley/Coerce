package io.coerce.networking.http.requests;

import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.sessions.HttpSession;

public interface HttpRequest extends HttpPayload {
    HttpRequestType getType();

    HttpSession getSession();

    String getLocation();

    String getHttpVersion();
}
