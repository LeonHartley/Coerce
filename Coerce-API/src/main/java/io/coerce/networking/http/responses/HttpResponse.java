package io.coerce.networking.http.responses;

import java.util.Map;

public interface HttpResponse {
    String getContentType();

    void setContentType(final String contentType);

    Map<String, String> getHeaders();

    void send(final int responseCode, final String string);

    void send(final String string);
}
