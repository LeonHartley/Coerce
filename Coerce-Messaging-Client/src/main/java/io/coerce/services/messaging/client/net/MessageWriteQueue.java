package io.coerce.services.messaging.client.net;

import io.coerce.networking.channels.NetworkChannel;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MessageWriteQueue implements Runnable {
    private final BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(1000);

    private final NetworkChannel networkChannel;

    public MessageWriteQueue(final NetworkChannel channel) {
        this.networkChannel = channel;
    }

    @Override
    public void run() {
        while (true) {
            final Object object = this.queue.poll();

            this.networkChannel.writeAndFlush(object);
        }
    }
}
