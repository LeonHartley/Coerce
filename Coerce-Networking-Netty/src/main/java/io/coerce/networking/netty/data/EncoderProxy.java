package io.coerce.networking.netty.data;

import io.coerce.networking.codec.ObjectEncoder;
import io.coerce.networking.netty.NettyNetworkingService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class EncoderProxy extends MessageToByteEncoder<Object> {

    private final ObjectEncoder objectEncoder;

    public EncoderProxy(final ObjectEncoder objectEncoder) {
        this.objectEncoder = objectEncoder;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        try {
            this.objectEncoder.encode(o, channelHandlerContext.channel().attr(NettyNetworkingService.channelAttributeKey).get(),
                    new ByteBufProxy(byteBuf));

            if(this.objectEncoder.hasDelimiter()) {
                byteBuf.writeBytes(this.objectEncoder.getDelimiter());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
