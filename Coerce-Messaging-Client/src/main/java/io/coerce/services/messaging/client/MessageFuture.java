package io.coerce.services.messaging.client;

import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.MessageResponse;

import java.util.concurrent.*;

public class MessageFuture<T extends MessageResponse> implements Future {

    private final MessageRequest<T> messageRequest;
    private final BlockingQueue<T> queue = new ArrayBlockingQueue<T>(1);

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

    public void setResponse(T response) {
        this.queue.offer(response);
    }
}
