package io.coerce.services.messaging.server.net;

import com.google.inject.Inject;
import io.coerce.messaging.commands.Command;
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
    private final SessionManager sessionManager;

    private final Logger log = LogManager.getLogger(MessagingChannelHandler.class);
    private volatile int sentMessages = 0;

    @Inject
    public MessagingChannelHandler(final JsonMessageEncoder messageEncoder, final JsonMessageDecoder messageDecoder,
                                   SessionManager sessionManager) {
        this.messageEncoder = messageEncoder;
        this.messageDecoder = messageDecoder;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onChannelActive(NetworkChannel networkChannel) {
        log.info("Channel connected {}", networkChannel.getId());
    }

    @Override
    public void onChannelInactive(NetworkChannel networkChannel) {
        if (networkChannel.getAttachment(Session.class) != null) {
            this.sessionManager.removeSession(networkChannel.getAttachment(Session.class).getAlias());
        }

        log.info("Channel disconnected {}", networkChannel.getId());
    }

    @Override
    public void onChannelError(Throwable error, NetworkChannel networkChannel) {
        log.error("Error caught in networking", error);
    }

    @Override
    public void onMessageReceived(StringMessage message, NetworkChannel networkChannel) {
        if (message.getPayloadType().equals(Command.INITIALISE)) {
            if(networkChannel.getAttachment(Session.class) == null) {
                final String serviceAlias = message.getPayload();

                log.info("Service connected {}", serviceAlias, networkChannel.getId());
                final Session session = this.sessionManager.createSession(serviceAlias, networkChannel);

                networkChannel.addAttachment(session);
            }
            return;
        }

        try {
            final Session session = networkChannel.getAttachment(Session.class);
            final Session targetSession = this.sessionManager.getSession(message.getTarget());

            if(session != null) {
                session.getTotalSentMessages().incrementAndGet();
            }

            if (targetSession != null) {
                targetSession.getTotalReceivedMessages().incrementAndGet();
                targetSession.getNetworkChannel().writeAndFlush(message);
            }

            sentMessages++;

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
