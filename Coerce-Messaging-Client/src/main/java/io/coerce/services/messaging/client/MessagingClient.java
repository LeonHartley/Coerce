package io.coerce.services.messaging.client;

import com.google.inject.Inject;
import io.coerce.commons.config.Configuration;
import io.coerce.commons.json.JsonUtil;
import io.coerce.messaging.Message;
import io.coerce.messaging.types.ObjectMessage;
import io.coerce.networking.NetworkingClient;
import io.coerce.networking.netty.clients.NettyNetworkingClient;
import io.coerce.services.messaging.client.messages.MessageRegistry;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.MessageResponse;
import io.coerce.services.messaging.client.net.MessagingChannelHandler;
import io.coerce.services.messaging.core.net.codec.JsonMessageDecoder;
import io.coerce.services.messaging.core.net.codec.JsonMessageEncoder;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public final class MessagingClient {
    private final String alias;
    private final NetworkingClient client;
    private final MessagingChannelHandler channelHandler;

    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    @Inject
    public MessagingClient(final String alias, NetworkingClient client, MessagingChannelHandler channelHandler) {
        this.alias = alias;
        this.client = client;
        this.channelHandler = channelHandler;

        this.channelHandler.setServiceAlias(this.alias);
        this.client.configure(channelHandler);
    }

    public static MessagingClient create(final String alias, Configuration configuration) {
        return new MessagingClient(alias, new NettyNetworkingClient(configuration),
                new MessagingChannelHandler(new JsonMessageEncoder(), new JsonMessageDecoder()));
    }

    public void connect(String host, int port, Consumer<MessagingClient> onConnect) {
        this.client.connect(host, port, false, (client) -> {
            this.sendMessage(new ObjectMessage(UUID.randomUUID(), this.alias, "master", "java.lang.String", this.alias));

            onConnect.accept(this);
        });
    }

    public <T extends MessageRequest> void observe(Class<T> messageRequestClass, Consumer<T> consumer) {
        MessageRegistry.getInstance().observeForMessages(messageRequestClass, consumer);
    }

    public void submitRequest(final String destination, final MessageRequest messageRequest) {
        this.executorService.execute(() -> {
            MessageRegistry.getInstance().awaitResponse(messageRequest);

            messageRequest.setSender(this.alias);

            this.channelHandler.getLastChannel().writeAndFlush(new ObjectMessage(messageRequest.getMessageId(), this.alias,
                    destination, messageRequest.getClass().getName(), JsonUtil.getGsonInstance().toJson(messageRequest)));
        });
    }

    public void flushData() {
        this.channelHandler.getLastChannel().flush();
    }

    public void sendResponse(UUID messageId, final String destination, final MessageResponse messageResponse) {
        this.executorService.submit(() -> {
            try {
                this.channelHandler.getLastChannel().writeAndFlush(new ObjectMessage(messageId, this.alias,
                        destination, messageResponse.getClass().getName(), JsonUtil.getGsonInstance().toJson(messageResponse)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void sendMessage(Message message) {
        this.channelHandler.getLastChannel().writeAndFlush(message);
    }

    public String getAlias() {
        return this.alias;
    }
}
