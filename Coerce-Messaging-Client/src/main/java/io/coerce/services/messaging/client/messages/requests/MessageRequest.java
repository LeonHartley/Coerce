package io.coerce.services.messaging.client.messages.requests;

import io.coerce.commons.json.JsonExclude;
import io.coerce.services.messaging.client.MessageFuture;
import io.coerce.services.messaging.client.messages.response.MessageResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;
import java.util.concurrent.Future;

public abstract class MessageRequest<T extends MessageResponse> {
    private static final Logger log = LogManager.getLogger(MessageRequest.class.getName());

    @JsonExclude
    private MessageFuture<T> future;

    private final UUID messageId;
    private final long timestamp;
    private String responseClass;
    private String sender;

    public MessageRequest(UUID messageId, Class<T> responseClass) {
        this.messageId = messageId;
        this.responseClass = responseClass.getName();
        this.timestamp = System.currentTimeMillis();
    }

    public final UUID getMessageId() {
        return this.messageId;
    }

    public final Class<T> getResponseClass() throws ClassNotFoundException {
        return (Class<T>) Class.forName(this.responseClass);
    }

    public final void handleResponse(final Object data) throws Exception {
        final T object = this.getResponseClass().cast(data);

        try {
            if(this.future != null) {
                this.future.addListener(this::onResponseReceived);

                this.future.setResponse(object);
            }
        } catch (Exception e) {
            log.error("Error while handing response with ID {}", this.getMessageId(), e);
        }
    }

    protected void onResponseReceived(T response) {

    }

    public String getSender() {
        return this.sender;
    }

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public MessageFuture<T> getFuture() {
        return this.future;
    }

    public void setFuture(final MessageFuture<T> future) {
        this.future = future;
    }
}
