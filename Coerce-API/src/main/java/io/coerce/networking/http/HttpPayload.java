package io.coerce.networking.http;

import com.google.gson.JsonObject;

import java.util.Map;

public interface HttpPayload {
    String getHeader();

    Map<String, String> getHeaders();

    byte[] getData();

    JsonObject getDataAsJson();
}
