package io.coerce.networking.nio.clients.io;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NioChannelWriter {
    private static final Logger log = LogManager.getLogger(NioChannelWriter.class);

    // TODO: Dynamic buffer allocation
    private final AtomicInteger bytesToWrite = new AtomicInteger(0);
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    private final SocketChannel socketChannel;

    private final List<Consumer<SocketChannel>> onWriteComplete = Lists.newCopyOnWriteArrayList();

    private boolean needsFlush;

    public NioChannelWriter(final SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void write(final byte[] data) {
        // TODO: Allow custom codec

        this.needsFlush = true;
        this.bytesToWrite.addAndGet(data.length);
        this.byteBuffer.put(data);
    }

    public void writeAndThen(final byte[] data, final Consumer<SocketChannel> onWriteComplete) {
        this.write(data);

        this.onWriteComplete.add(onWriteComplete);
    }

    public void flush() throws IOException {
        int writtenBytes = this.socketChannel.write(this.byteBuffer);

        if (writtenBytes >= this.bytesToWrite.get()) {
            this.needsFlush = false;
        }

        for (Consumer<SocketChannel> onWriteComplete : this.onWriteComplete) {
            onWriteComplete.accept(this.socketChannel);
        }

        this.onWriteComplete.clear();

        log.debug("Wrote {} bytes to client", writtenBytes);
//
//        // Slice the buffer so we only have what's left to write
//        this.byteBuffer.slice();
    }

    public boolean needsFlush() {
        return this.needsFlush;
    }
}
