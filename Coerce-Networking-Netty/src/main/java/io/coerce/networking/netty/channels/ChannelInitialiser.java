package io.coerce.networking.netty.channels;

import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.netty.data.DecoderProxy;
import io.coerce.networking.netty.data.EncoderProxy;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.function.Consumer;

public class ChannelInitialiser extends ChannelInitializer<SocketChannel> {

    private final EventLoopGroup eventLoop;
    private final NetworkChannelHandler handler;

    private final Consumer<NetworkChannel> onConnect;

    public ChannelInitialiser(EventLoopGroup channelEventLoop, NetworkChannelHandler handler, Consumer<NetworkChannel> onConnect) {
        this.eventLoop = channelEventLoop;
        this.handler = handler;
        this.onConnect = onConnect;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.config().setTrafficClass(0x18);

        if(handler.getObjectEncoder().hasDelimiter()) {
            socketChannel.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE,
                    Unpooled.wrappedBuffer(handler.getObjectEncoder().getDelimiter())));
        }

        socketChannel.pipeline().addLast("encoder", new EncoderProxy(this.handler.getObjectEncoder()));
        socketChannel.pipeline().addLast("decoder", new DecoderProxy(this.handler.getObjectDecoder()));

        socketChannel.pipeline().addLast(this.eventLoop, "clientHandler", new ChannelHandler(this.handler, this.onConnect));
    }
}
