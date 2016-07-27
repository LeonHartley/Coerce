package io.coerce.networking.channels;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class NetworkChannel {
    private final Map<Class<?>, Object> attachments = new ConcurrentHashMap<>();

    public <T> T getAttachment(Class<T> attachmentClass) {
        if(this.attachments.containsKey(attachmentClass)) {
            return (T) this.attachments.get(attachmentClass);
        }

        return null;
    }

    public boolean hasAttachment(Class<?> attachmentClass) {
        return this.attachments.containsKey(attachmentClass);
    }

    public void removeAttachment(Class<?> attachmentClass) {
        this.attachments.remove(attachmentClass);
    }

    public void addAttachment(Object attachment) {
        this.attachments.put(attachment.getClass(), attachment);
    }

    public abstract UUID getId();

    public abstract void write(Object message);

    public abstract void writeAndFlush(Object message);

    public abstract void flush();

    public abstract void writeAndClose(Object message);

    public abstract void close();
}
