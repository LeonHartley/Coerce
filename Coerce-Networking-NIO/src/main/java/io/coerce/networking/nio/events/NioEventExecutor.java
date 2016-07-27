package io.coerce.networking.nio.events;

import com.google.inject.Inject;
import io.coerce.networking.nio.clients.NioChannel;
import io.coerce.networking.nio.clients.NioChannelFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectableChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class NioEventExecutor implements Runnable {
    private final Logger log;
    private final Thread eventThread;
    private final NioChannelFactory channelFactory;
    private final String eventExecutorId;
    private Selector selector;

    @Inject
    public NioEventExecutor(NioChannelFactory channelFactory) {
        this.eventExecutorId = NioEventExecutor.class.getSimpleName() + "#" + UUID.randomUUID();
        this.log = LogManager.getLogger(eventExecutorId);
        this.channelFactory = channelFactory;

        try {
            this.selector = Selector.open();
        } catch (Exception e) {
            log.error("Failed to open a new selector", e);
        }

        this.eventThread = new Thread(this);
        this.eventThread.setName(eventExecutorId);
        this.eventThread.start();

//        this.eventThread.setUncaughtExceptionHandler((t, e) -> {
//            e.printStackTrace();
//        });
    }

    public void register(AbstractSelectableChannel channel, GroupPurpose purpose) {
        this.register(channel, purpose, null);
    }

    public void register(AbstractSelectableChannel channel, GroupPurpose purpose, Object attachment) {
        int ops = 0;

        switch (purpose) {
            case ACCEPT:
            default:
                ops = SelectionKey.OP_ACCEPT;
                break;

            case IO:
                ops = SelectionKey.OP_READ | SelectionKey.OP_WRITE;
                break;
        }

        try {
            if (attachment != null) {
                channel.register(this.selector, ops, attachment);
            } else {
                channel.register(this.selector, ops);
            }
        } catch (Exception e) {
            log.warn("Error during selector registration", e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Select with a 1ms timeout.
                this.selector.select(1);

                final Set<SelectionKey> readyEvents = this.selector.selectedKeys();
                final Iterator<SelectionKey> events = readyEvents.iterator();

                while (events.hasNext()) {
                    final SelectionKey selectionKey = events.next();

                    if (!selectionKey.isValid()) {
                        continue;
                    }

                    final int readyOperations = selectionKey.readyOps();

                    try {
                        if ((readyOperations & SelectionKey.OP_ACCEPT) > 0) {
                            this.accept(selectionKey);
                        } else if ((readyOperations & SelectionKey.OP_READ) > 0) {
                            this.read(selectionKey);
                        } else if ((readyOperations & SelectionKey.OP_WRITE) > 0) {
                            this.write(selectionKey);
                        }
                    } catch (Exception e) {
                        selectionKey.channel().close();
                        log.warn("Error during selection", e);
                    } finally {
                        events.remove();
                    }
                }

                readyEvents.clear();
            }
        } catch (Exception e) {
            log.warn("Error during event execution", e);
        }
    }

    private void read(final SelectionKey key) throws Exception {
        // Read data
        SocketChannel client = (SocketChannel) key.channel();

        if (client != null) {
            final NioChannel nioChannel = ((NioChannel) key.attachment());

            ByteBuffer output = nioChannel.getReader().getByteBuffer();
            client.read(output);

            log.debug("Received: " + new String(output.array(), Charset.forName("UTF-8")));

            nioChannel.onReceiveData();
            output.clear();
        }
    }

    private void write(final SelectionKey key) throws Exception {
        final NioChannel nioChannel = (NioChannel) key.attachment();

        if (nioChannel.getWriter().needsFlush()) {
            nioChannel.getWriter().flush();
        }

//        log.debug("Written bytes: {}, total bytes: {}", writtenBytes, output.limit());
//        output.clear();

        key.cancel();
    }

    private void accept(final SelectionKey key) throws Exception {
        ServerSocketChannel server = (ServerSocketChannel) key.channel();
        SocketChannel client = server.accept();

        if (client != null) {
            log.info("Accepted client {}, {}", client.getRemoteAddress(), client.getClass());
            client.configureBlocking(false);

            final NioChannel nioChannel = this.channelFactory.createChannel(client);
            nioChannel.onConnection();
        }
    }

    public String getEventExecutorId() {
        return eventExecutorId;
    }
}
