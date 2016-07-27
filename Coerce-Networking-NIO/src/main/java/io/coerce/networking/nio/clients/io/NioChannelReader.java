package io.coerce.networking.nio.clients.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioChannelReader {
    private static final Logger log = LogManager.getLogger(NioChannelWriter.class);

    private final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private final SocketChannel socketChannel;

    public NioChannelReader(final SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }
}
