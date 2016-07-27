package io.coerce.networking.netty.channels;

import io.coerce.networking.channels.NetworkChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.UUID;

public class ChannelProxy extends NetworkChannel {

    private final UUID id;
    private final Channel channel;

    public ChannelProxy(final UUID id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void write(Object message) {
        this.channel.write(message);
    }

    @Override
    public void writeAndFlush(Object message) {
        this.channel.writeAndFlush(message);
    }

    @Override
    public void flush() {
        this.channel.flush();
    }

    @Override
    public void writeAndClose(Object message) {
        this.channel.writeAndFlush(message).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void close() {
        this.channel.close();
    }
}
