package io.coerce.networking.netty.clients;


import com.google.inject.Inject;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingClient;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.netty.channels.ChannelInitialiser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NettyNetworkingClient implements NetworkingClient {
    private static final Logger log = LogManager.getLogger(NettyNetworkingClient.class);

    private final Configuration configuration;
    private final Bootstrap bootstrap;
    private NetworkChannelHandler handler;
    private EventLoopGroup eventLoopGroup;

    @Inject
    public NettyNetworkingClient(CoerceConfiguration configuration) {
        this.configuration = configuration.getObject("nettyClient");
        this.bootstrap = new Bootstrap();
    }

    @Override
    public void configure(NetworkChannelHandler handler) {
        this.handler = handler;

        final boolean useEpoll = Epoll.isAvailable() && this.configuration.getBoolean("epoll");

        this.eventLoopGroup = useEpoll ? new EpollEventLoopGroup(this.configuration.getInt("threads")) :
                new NioEventLoopGroup(this.configuration.getInt("threads"));

        bootstrap.group(this.eventLoopGroup);
        bootstrap.channel(useEpoll ? EpollSocketChannel.class : NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
    }

    @Override
    public void connect(String host, int port, boolean autoReconnect, Consumer<NetworkChannel> onConnect) {
        this.bootstrap.remoteAddress(host, port);
        bootstrap.handler(new ChannelInitialiser(this.eventLoopGroup, this.handler, onConnect));

        this.bootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (!channelFuture.isSuccess()) {
                    if (!(channelFuture.cause() instanceof ConnectException)) {
                        channelFuture.cause().printStackTrace();
                    }

                    // we disconnected, try reconnect in 100ms - todo: make this configurable
                    eventLoopGroup.schedule(() ->
                            connect(host, port, autoReconnect, onConnect), 100, TimeUnit.MILLISECONDS);
                } else {
                    if (autoReconnect) {
                        channelFuture.channel().closeFuture().addListener(
                                (ChannelFutureListener) future -> connect(host, port, autoReconnect, onConnect));
                    }
                }
            }
        });
    }
}