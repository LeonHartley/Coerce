package io.coerce.networking.http.responses;

import io.coerce.networking.http.cookies.Cookie;

import java.util.Map;

public interface HttpResponse {
    String getContentType();

    void setContentType(final String contentType);

    void send(final String string);

    void send(final byte[] bytes);

    void renderView(final String view, Map<String, Object> model);

    void setHeader(final String key, final String value);

    String getHeader(final String key);

    void setCookie(Cookie cookie);

    boolean hasCookie(final String key);

    HttpResponseCode getResponseCode();

    void setResponseCode(HttpResponseCode type);

    void redirect(final String location);
}
