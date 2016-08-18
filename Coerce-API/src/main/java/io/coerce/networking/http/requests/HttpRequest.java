package io.coerce.networking.http.requests;

import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.sessions.HttpSession;

import java.util.Map;

public interface HttpRequest extends HttpPayload {
    HttpRequestType getType();

    HttpSession getSession();

    String getLocation();

    String getHttpVersion();

    String getUrlParameter(final String key);

    String getQueryParameter(final String key);

    boolean hasQueryParameter(final String key);

    Map<String, Cookie> getCookies();
}
