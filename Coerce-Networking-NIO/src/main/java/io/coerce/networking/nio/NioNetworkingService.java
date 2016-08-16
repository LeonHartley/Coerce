package io.coerce.networking.nio;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.nio.clients.NioChannelFactory;
import io.coerce.networking.nio.clients.NioChannelGroup;
import io.coerce.networking.nio.events.NioEventExecutorGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.channels.ServerSocketChannel;

@Singleton
public class NioNetworkingService implements NetworkingService {
    private static final Logger log = LogManager.getLogger(NioNetworkingService.class);
    private final NioChannelFactory channelFactory;
    private final NioChannelGroup channelGroup;
    private final NioEventExecutorGroup acceptGroup;
    private ServerSocketChannel serverSocketChannel;

    @Inject
    public NioNetworkingService(NioChannelFactory channelFactory, NioChannelGroup channelGroup,
                                NioEventExecutorGroup acceptGroup, NioEventExecutorGroup ioGroup) {
        this.channelFactory = channelFactory;
        this.channelGroup = channelGroup;
        this.acceptGroup = acceptGroup;

        this.channelFactory.initialise(ioGroup);
    }

    @Override
    public void initialise(NetworkChannelHandler channelHandler) {

    }

    @Override
    public void startService(String host, int port) {
//        try {
//            this.serverSocketChannel = ServerSocketChannel.open();
//            this.serverSocketChannel.configureBlocking(false);
//
//            this.acceptGroup.register(this.serverSocketChannel, GroupPurpose.ACCEPT);
//
//            this.serverSocketChannel.socket().bind(new InetSocketAddress(host, port));
//            Thread.currentThread().join();
//        } catch (Exception e) {
//            log.error("Failed to start NetworkingService on address: {}:{}", host, port, e);
//        }
    }
}
