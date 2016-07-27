package io.coerce.messaging;

import java.util.UUID;

public interface Message {
    UUID getMessageId();

    String getSender();

    String getTarget();

    String getPayloadType();

    Object getPayload();
}
