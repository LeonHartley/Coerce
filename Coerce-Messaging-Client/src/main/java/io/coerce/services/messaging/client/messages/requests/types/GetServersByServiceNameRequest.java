package io.coerce.services.messaging.client.messages.requests.types;

import io.coerce.commons.json.JsonExclude;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.types.GetServersByServiceNameResponse;

import java.util.UUID;
import java.util.function.Consumer;

public class GetServersByServiceNameRequest extends MessageRequest<GetServersByServiceNameResponse> {

    @JsonExclude
    private final Consumer<GetServersByServiceNameResponse> onCompletion;

    private final String namePattern;

    public GetServersByServiceNameRequest(final String namePattern, final Consumer<GetServersByServiceNameResponse> onCompletion) {
        super(UUID.randomUUID(), GetServersByServiceNameResponse.class);

        this.namePattern = namePattern;
        this.onCompletion = onCompletion;
    }

    @Override
    protected void onResponseReceived(GetServersByServiceNameResponse response) {
        this.onCompletion.accept(response);
    }

    public String getNamePattern() {
        return namePattern;
    }
}
