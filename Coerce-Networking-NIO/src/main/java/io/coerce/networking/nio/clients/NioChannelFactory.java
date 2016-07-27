package io.coerce.networking.nio.clients;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.coerce.networking.nio.clients.io.NioChannelReader;
import io.coerce.networking.nio.clients.io.NioChannelWriter;
import io.coerce.networking.nio.events.GroupPurpose;
import io.coerce.networking.nio.events.NioEventExecutorGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.channels.SocketChannel;

@Singleton
public class NioChannelFactory {
    private final Logger log = LogManager.getLogger(NioChannelFactory.class);
    private NioEventExecutorGroup ioGroup;

    @Inject
    public NioChannelFactory() {

    }

    public void initialise(NioEventExecutorGroup ioGroup) {
        this.ioGroup = ioGroup;
    }

    public NioChannel createChannel(SocketChannel socketChannel) {
        // TODO: Clean up the factory shit, we can use Guice
        final NioChannel nioChannel = new NioChannel(socketChannel,
                new NioChannelWriter(socketChannel),
                new NioChannelReader(socketChannel));
        log.debug("Created NioChannel for client {} on address {}", nioChannel.getChannelId(), nioChannel.getAddress());

        this.ioGroup.register(socketChannel, GroupPurpose.IO, nioChannel);
        return nioChannel;
    }

}
