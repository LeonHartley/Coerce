package io.coerce.services.messaging.server.net;

import com.google.inject.Inject;
import io.coerce.messaging.types.StringMessage;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.channels.NetworkChannelHandler;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.codec.ObjectEncoder;
import io.coerce.services.messaging.core.net.codec.JsonMessageDecoder;
import io.coerce.services.messaging.core.net.codec.JsonMessageEncoder;
import io.coerce.services.messaging.server.sessions.Session;
import io.coerce.services.messaging.server.sessions.SessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessagingChannelHandler implements NetworkChannelHandler<StringMessage> {

    private final JsonMessageEncoder messageEncoder;
    private final JsonMessageDecoder messageDecoder;

    private final Logger log = LogManager.getLogger(MessagingChannelHandler.class);
    private volatile int sentMessages = 0;

    @Inject
    public MessagingChannelHandler(final JsonMessageEncoder messageEncoder, final JsonMessageDecoder messageDecoder) {
        this.messageEncoder = messageEncoder;
        this.messageDecoder = messageDecoder;
    }

    @Override
    public void onChannelActive(NetworkChannel networkChannel) {
        log.info("Channel connected {}", networkChannel.getId());
    }

    @Override
    public void onChannelInactive(NetworkChannel networkChannel) {
        if (networkChannel.getAttachment(Session.class) != null) {
            SessionManager.getInstance().removeSession(networkChannel.getAttachment(Session.class).getAlias());
        }

        log.info("Channel disconnected {}", networkChannel.getId());
    }

    @Override
    public void onChannelError(Throwable error, NetworkChannel networkChannel) {
        log.error("Error caught in networking", error);
    }

    @Override
    public void onMessageReceived(StringMessage message, NetworkChannel networkChannel) {
        if (message.getPayloadType().equals("java.lang.String") && networkChannel.getAttachment(Session.class) == null) {
            final String serviceAlias = (String) message.getPayload();

            log.info("Service connected {}", serviceAlias, networkChannel.getId());
            final Session session = SessionManager.getInstance().createSession(serviceAlias, networkChannel);

            networkChannel.addAttachment(session);
            return;
        }

        try {
            final Session session = SessionManager.getInstance().getSession(message.getTarget());

            if (session != null) {
                session.getNetworkChannel().writeAndFlush(message);
            }

            sentMessages++;

            // For now, we'll echo it back to the sender, soon we'll send it to the target.
            log.info("Message [" + sentMessages + "] received {}", message.getMessageId());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return null;
    }
}
