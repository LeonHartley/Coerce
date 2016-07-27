package io.coerce.services.messaging.client.messages;

import io.coerce.commons.json.JsonUtil;
import io.coerce.messaging.types.StringMessage;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class MessageRegistry {
    private static final MessageRegistry registry = new MessageRegistry();

    // message observers
    private final Map<Class<? extends MessageRequest>, List<Consumer<? extends MessageRequest>>> messageObservers;

    // messages awaiting response
    private final Map<UUID, MessageRequest> entries;

    public MessageRegistry() {
        this.entries = new ConcurrentHashMap<>();
        this.messageObservers = new ConcurrentHashMap<>();
    }

    public static MessageRegistry getInstance() {
        return registry;
    }

    public void awaitResponse(MessageRequest messageRequest) {
        this.entries.put(messageRequest.getMessageId(), messageRequest);
    }

    public MessageRequest getAwaitedRequest(final UUID id) {
        try {
            return this.entries.get(id);
        } finally {
            this.entries.remove(id);
        }
    }

    public <T extends MessageRequest> void processObservers(Class<T> clazz, final StringMessage message) {
        try {
            for (Consumer<? extends MessageRequest> requestConsumer : this.messageObservers.get(clazz)) {
                final Consumer<T> consumer = ((Consumer<T>) requestConsumer);

                consumer.accept(JsonUtil.getGsonInstance().fromJson(message.getPayload(), clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends MessageRequest> void observeForMessages(Class<T> messageRequestClass, Consumer<T> consumer) {
        if (!this.messageObservers.containsKey(messageRequestClass)) {
            this.messageObservers.put(messageRequestClass, new CopyOnWriteArrayList<>());
        }

        this.messageObservers.get(messageRequestClass).add(consumer);
    }

    public boolean hasObservers(final Class<? extends MessageRequest> requestClass) {
        return this.messageObservers.containsKey(requestClass) && this.messageObservers.get(requestClass).size() != 0;
    }
}
