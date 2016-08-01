package io.coerce.networking.http.responses;

import io.coerce.networking.http.HttpPayload;

import java.util.Map;

public interface HttpResponse {
    String getContentType();

    void setContentType(final String contentType);

    void send(final String string);

    void renderView(final String view, Map<String, Object> model);

    int getResponseCode();

    void setResponseCode(int responseCode);
}
