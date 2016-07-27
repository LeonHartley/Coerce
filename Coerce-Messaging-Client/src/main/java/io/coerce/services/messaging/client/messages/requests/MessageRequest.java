package io.coerce.services.messaging.client.messages.requests;

import io.coerce.commons.json.JsonUtil;
import io.coerce.services.messaging.client.messages.response.MessageResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public abstract class MessageRequest<T extends MessageResponse> {
    private static final Logger log = LogManager.getLogger(MessageRequest.class.getName());

    private final UUID messageId;
    private String responseClass;
    private String sender;

    private final long timestamp;

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
            this.onResponseReceived(object);
        } catch(Exception e) {
            log.error("Error while handing response with ID {}", this.getMessageId(), e);
        }
    }

    protected abstract void onResponseReceived(T response);

    public void setSender(final String sender) {
        this.sender = sender;
    }

    public String getSender() {
        return this.sender;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
