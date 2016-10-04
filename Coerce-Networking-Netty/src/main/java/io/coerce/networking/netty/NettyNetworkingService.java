package io.coerce.networking.netty;

import com.google.inject.Inject;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.netty.channels.ChannelInitialiser;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultMessageSizeEstimator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.function.Consumer;

public class NettyNetworkingService implements NetworkingService {

    public static final AttributeKey<NetworkChannel> channelAttributeKey = AttributeKey.valueOf("NetworkChannel.attr");

    private final Configuration configuration;

    private NetworkChannelHandler channelHandler;
    private ServerBootstrap serverBootstrap;
    private Logger log = LogManager.getLogger(NettyNetworkingService.class);

    @Inject
    public NettyNetworkingService(final CoerceConfiguration configuration) {
        this.configuration = configuration.getObject("nettyServer");
    }

    @Override
    public void initialise(NetworkChannelHandler channelHandler) {
        this.channelHandler = channelHandler;

        final boolean useEpoll = this.configuration.getBoolean("epoll") && Epoll.isAvailable();

        EventLoopGroup acceptGroup = useEpoll ? new EpollEventLoopGroup(this.configuration.getInt("acceptGroup")) :
                new NioEventLoopGroup(this.configuration.getInt("acceptGroup"));

        EventLoopGroup ioGroup = useEpoll ? new EpollEventLoopGroup(this.configuration.getInt("ioGroup")) :
                new NioEventLoopGroup(this.configuration.getInt("ioGroup"));

        EventLoopGroup channelGroup = useEpoll ? new EpollEventLoopGroup(this.configuration.getInt("channelGroup")) :
                new NioEventLoopGroup(this.configuration.getInt("channelGroup"));

        this.serverBootstrap = new ServerBootstrap()
                .group(acceptGroup, ioGroup)
                .channel(useEpoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .childHandler(new ChannelInitialiser(channelGroup, this.channelHandler, null))
                .option(ChannelOption.SO_BACKLOG, this.configuration.getInt("backlog"))
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.MESSAGE_SIZE_ESTIMATOR, DefaultMessageSizeEstimator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, this.configuration.getBoolean("tcpNoDelay"))
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
    }

    @Override
    public void startService(String host, int port, Consumer<NetworkingService> onServiceStarted) {
        this.serverBootstrap.bind(new InetSocketAddress(host, port)).addListener(objectFuture -> {
            if (!objectFuture.isSuccess()) {
                log.warn("Failed to initialise networking server on tcp://{}:{}/", host, port);
            } else {
                onServiceStarted.accept(this);
            }
        });
    }
}
