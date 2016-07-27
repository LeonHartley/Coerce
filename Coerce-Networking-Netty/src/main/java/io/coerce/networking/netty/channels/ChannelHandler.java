package io.coerce.networking.netty.channels;

import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.netty.NettyNetworkingService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;
import java.util.function.Consumer;

@io.netty.channel.ChannelHandler.Sharable
public class ChannelHandler extends SimpleChannelInboundHandler<Object> {

    private NetworkChannelHandler channelHandler;

    private Consumer<NetworkChannel> onChannelActiveConsumer;

    public ChannelHandler(NetworkChannelHandler childHandler, Consumer<NetworkChannel> onChannelActiveConsumer) {
        this.channelHandler = childHandler;
        this.onChannelActiveConsumer = onChannelActiveConsumer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // Create NetworkChannel instance, set it as an attribute then we call the channelHandler event "onChannelActive"
        final NetworkChannel networkChannel = new ChannelProxy(UUID.randomUUID(), ctx.channel());
        ctx.channel().attr(NettyNetworkingService.channelAttributeKey).set(networkChannel);

        this.channelHandler.onChannelActive(networkChannel);

        if (this.onChannelActiveConsumer != null) {
            this.onChannelActiveConsumer.accept(networkChannel);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // Call the channelHandler event "onChannelInactive" then remove the attribute reference to the NetworkChannel
        // then we need to dispose of the NetworkChannel object.
        final NetworkChannel networkChannel = this.getNetworkChannel(ctx);

        if (networkChannel == null) {
            return;
        }

        this.channelHandler.onChannelInactive(networkChannel);
        ctx.channel().attr(NettyNetworkingService.channelAttributeKey).remove();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            ctx.close();
        }

        this.channelHandler.onChannelError(cause, this.getNetworkChannel(ctx));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        this.channelHandler.onMessageReceived(o, this.getNetworkChannel(ctx));
    }

    private NetworkChannel getNetworkChannel(ChannelHandlerContext ctx) {
        return ctx.channel().attr(NettyNetworkingService.channelAttributeKey).get();
    }
}
