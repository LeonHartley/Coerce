package io.coerce.messaging;

import java.io.Serializable;
import java.util.UUID;

public interface Message extends Serializable {
    UUID getMessageId();

    String getSender();

    String getTarget();

    String getPayloadType();

    Object getPayload();
}
