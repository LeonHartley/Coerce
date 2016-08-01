package io.coerce.networking.http.responses;

import java.util.Map;

public interface HttpResponse {
    String getContentType();

    void setContentType(final String contentType);

    void send(final String string);

    void renderView(final String view, Map<String, Object> model);

    HttpResponseCode getResponseCode();

    void setResponseCode(HttpResponseCode type);
}
