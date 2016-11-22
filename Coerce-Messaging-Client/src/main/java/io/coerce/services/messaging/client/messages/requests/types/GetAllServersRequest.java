package io.coerce.services.messaging.client.messages.requests.types;

import io.coerce.commons.json.JsonExclude;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.types.GetAllServersResponse;

import java.util.UUID;
import java.util.function.Consumer;

public class GetAllServersRequest extends MessageRequest<GetAllServersResponse> {

    public GetAllServersRequest() {
        super(UUID.randomUUID(), GetAllServersResponse.class);
    }
}
