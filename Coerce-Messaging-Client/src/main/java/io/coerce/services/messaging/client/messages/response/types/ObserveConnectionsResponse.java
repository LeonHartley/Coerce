package io.coerce.services.messaging.client.messages.response.types;

import io.coerce.services.messaging.client.messages.response.MessageResponse;

public class ObserveConnectionsResponse implements MessageResponse {
    private boolean isObserving = false;

    public ObserveConnectionsResponse(final boolean isObserving) {
        this.isObserving = isObserving;
    }

    public boolean isObserving() {
        return this.isObserving;
    }
}
