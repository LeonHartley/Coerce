package io.coerce.services.messaging.example.boot.messages.time;

import io.coerce.commons.json.JsonExclude;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.UUID;
import java.util.function.Consumer;

public class TimeMessageRequest extends MessageRequest<TimeMessageResponse> {
    @JsonExclude
    private final Consumer<String> timeConsumer;

    public TimeMessageRequest(final Consumer<String> timeConsumer) {
        super(UUID.randomUUID(), TimeMessageResponse.class);

        this.timeConsumer = timeConsumer;
    }

    @Override
    protected void onResponseReceived(TimeMessageResponse response) {
        System.out.println("Took " + (System.currentTimeMillis() - this.getTimestamp()) + "ms to get a response");

        this.timeConsumer.accept(response.getTime());
    }
}
