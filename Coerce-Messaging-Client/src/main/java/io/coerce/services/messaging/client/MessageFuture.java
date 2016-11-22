package io.coerce.services.messaging.client;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.MessageResponse;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class MessageFuture<T extends MessageResponse> implements Future {

    private final MessageRequest<T> messageRequest;
    private final BlockingQueue<T> queue = new ArrayBlockingQueue<T>(1);

    private final List<Consumer<T>> listeners = new CopyOnWriteArrayList<>();

    private T response;
    private boolean isDone = false;

    public MessageFuture(final MessageRequest<T> request) {
        this.messageRequest = request;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return this.isDone;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        if(this.response != null) {
            return this.response;
        }

        this.response = this.queue.take();

        return this.response;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if(this.response != null) {
            return this.response;
        }

        this.response = this.queue.poll(timeout, unit);

        return this.response;
    }

    public void addListener(Consumer<T> consumer) {
        this.listeners.add(consumer);
    }

    public void setResponse(T response) {
        for(Consumer<T> consumer : this.listeners) {
            consumer.accept(response);
        }

        this.listeners.clear();

        this.queue.offer(response);
    }
}
