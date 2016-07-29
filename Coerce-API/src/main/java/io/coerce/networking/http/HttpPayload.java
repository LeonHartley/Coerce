package io.coerce.networking.http;

import java.util.Map;

public interface HttpPayload {
    String getHeader();

    Map<String, String> getHeaders();

    byte[] getData();
}
