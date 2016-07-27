package io.coerce.networking.netty.data;

import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.netty.NettyNetworkingService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class DecoderProxy extends MessageToMessageDecoder<ByteBuf> {

    private final ObjectDecoder objectDecoder;

    public DecoderProxy(final ObjectDecoder objectDecoder) {
        this.objectDecoder = objectDecoder;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try {
            final ByteBufProxy buf = new ByteBufProxy(byteBuf);

            Object object = this.objectDecoder.decode(buf,
                    channelHandlerContext.channel().attr(NettyNetworkingService.channelAttributeKey).get());

            buf.dispose();

            if (object != null) {
                list.add(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
