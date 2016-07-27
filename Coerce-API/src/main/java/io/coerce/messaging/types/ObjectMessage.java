package io.coerce.messaging.types;

import com.google.gson.Gson;
import io.coerce.messaging.Message;
import io.coerce.networking.codec.strings.StringEncoder;

import java.util.UUID;

public class ObjectMessage implements Message {
    private static final Gson gson = new Gson();

    private final UUID id;
    private final String sender;
    private final String target;

    private final String payloadType;
    private final String messagePayload;

    public ObjectMessage(UUID id, String sender, String target, String payloadType, String payload) {
        this.id = id;
        this.sender = sender;
        this.target = target;
        this.payloadType = payloadType;
        this.messagePayload = payload;
    }

    @Override
    public UUID getMessageId() {
        return this.id;
    }

    @Override
    public String getSender() {
        return this.sender;
    }

    @Override
    public String getTarget() {
        return this.target;
    }

    @Override
    public String getPayloadType() {
        return this.payloadType;
    }

    @Override
    public String getPayload() {
        return this.messagePayload;
    }
}
