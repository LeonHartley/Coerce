package io.coerce.services.messaging.client.messages.response.types;

import io.coerce.services.messaging.client.messages.response.MessageResponse;

import java.util.List;

public class GetAllServersResponse implements MessageResponse {
    private final List<String> services;

    public GetAllServersResponse(List<String> services) {
        this.services = services;
    }

    public List<String> getServices() {
        return services;
    }
}
