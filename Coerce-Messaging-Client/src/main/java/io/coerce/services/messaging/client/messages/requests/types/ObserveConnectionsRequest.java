package io.coerce.services.messaging.client.messages.requests.types;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.types.ObserveConnectionsResponse;

import java.util.UUID;

public class ObserveConnectionsRequest extends MessageRequest<ObserveConnectionsResponse> {
    public ObserveConnectionsRequest() {
        super(UUID.randomUUID(), ObserveConnectionsResponse.class);
    }
}
