package io.coerce.services.messaging.client.net;

import io.coerce.commons.json.JsonUtil;
import io.coerce.messaging.types.StringMessage;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.codec.ObjectEncoder;
import io.coerce.services.messaging.client.messages.MessageRegistry;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.core.net.codec.JsonMessageDecoder;
import io.coerce.services.messaging.core.net.codec.JsonMessageEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessagingChannelHandler implements NetworkChannelHandler<StringMessage> {

    private final JsonMessageEncoder messageEncoder;
    private final JsonMessageDecoder messageDecoder;
    private final Logger log = LogManager.getLogger(MessagingChannelHandler.class.getName());
    private String serviceAlias;
    private NetworkChannel networkChannel;

    public MessagingChannelHandler(final JsonMessageEncoder messageEncoder, final JsonMessageDecoder messageDecoder) {
        this.messageEncoder = messageEncoder;
        this.messageDecoder = messageDecoder;
    }

    @Override
    public void onChannelActive(NetworkChannel networkChannel) {
        if (this.networkChannel == null || networkChannel.getId() != this.networkChannel.getId()) {
            this.networkChannel = networkChannel;
        }
    }

    @Override
    public void onChannelInactive(NetworkChannel networkChannel) {

    }

    @Override
    public void onChannelError(Throwable error, NetworkChannel networkChannel) {
        error.printStackTrace();
    }

    @Override
    public void onMessageReceived(StringMessage message, NetworkChannel networkChannel) {
        try {
            final Class<?> messageClazz = Class.forName(message.getPayloadType());

            try {
                // try and cast the message class to a usable type.
                final Class<? extends MessageRequest> requestClass = (Class<? extends MessageRequest>) messageClazz;

                if (requestClass != null) {
                    if (MessageRegistry.getInstance().hasObservers(requestClass)) {
                        MessageRegistry.getInstance().processObservers(requestClass, message);
                        return;
                    }
                }
            } catch (Exception ignored) {
                // ignore the exception, we don't need to worry about it.. just carry on
            }

            final MessageRequest entry = MessageRegistry.getInstance().getAwaitedRequest(message.getMessageId());

            if (entry != null) {
                // TODO: Submit this to a separate thread pool or something
                entry.handleResponse(JsonUtil.getGsonInstance().fromJson(message.getPayload(), entry.getResponseClass()));
            }
        } catch (Exception e) {
            log.error("Error on receiving message data", e);
        }
    }

    public String getServiceAlias() {
        return this.serviceAlias;
    }

    public void setServiceAlias(final String serviceAlias) {
        this.serviceAlias = serviceAlias;
    }

    @Override
    public ObjectEncoder<StringMessage> getObjectEncoder() {
        return this.messageEncoder;
    }

    @Override
    public ObjectDecoder<StringMessage> getObjectDecoder() {
        return this.messageDecoder;
    }

    @Override
    public NetworkChannel getLastChannel() {
        return this.networkChannel;
    }
}
