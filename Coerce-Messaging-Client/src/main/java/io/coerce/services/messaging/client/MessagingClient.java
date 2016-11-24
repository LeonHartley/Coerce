package io.coerce.services.messaging.client;

import com.google.inject.Inject;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.json.JsonUtil;
import io.coerce.messaging.Message;
import io.coerce.messaging.commands.Command;
import io.coerce.messaging.types.StringMessage;
import io.coerce.networking.NetworkingClient;
import io.coerce.networking.netty.clients.NettyNetworkingClient;
import io.coerce.services.messaging.client.messages.MessageRegistry;
import io.coerce.services.messaging.client.messages.requests.MessageRequest;
import io.coerce.services.messaging.client.messages.response.MessageResponse;
import io.coerce.services.messaging.client.net.MessagingChannelHandler;
import io.coerce.services.messaging.core.net.codec.JsonMessageDecoder;
import io.coerce.services.messaging.core.net.codec.JsonMessageEncoder;

import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Consumer;

public final class MessagingClient {
    private final String alias;
    private final NetworkingClient client;
    private final MessagingChannelHandler channelHandler;

    private final ExecutorService executorService;

    @Inject
    public MessagingClient(final String alias, NetworkingClient client, MessagingChannelHandler channelHandler, ExecutorService executorService) {
        this.alias = alias;
        this.client = client;
        this.channelHandler = channelHandler;
        this.executorService = executorService;

        this.channelHandler.setServiceAlias(this.alias);
        this.client.configure(channelHandler);
    }

    public static MessagingClient create(final String alias, CoerceConfiguration configuration) {
        final ExecutorService executorService = Executors.newFixedThreadPool(4);

        return new MessagingClient(alias, new NettyNetworkingClient(configuration),
                new MessagingChannelHandler(new JsonMessageEncoder(), new JsonMessageDecoder(), executorService), executorService);
    }

    public void connect(String host, int port, Consumer<MessagingClient> onConnect) {
        this.client.connect(host, port, true, (client) -> {
            this.sendMessage(new StringMessage(UUID.randomUUID(), this.alias, "master", Command.INITIALISE, this.alias));

            this.executorService.submit(() -> {
                onConnect.accept(this);
            });
        });
    }

    public <T extends MessageRequest> void observe(Class<T> messageRequestClass, Consumer<T> consumer) {
        MessageRegistry.getInstance().observeForMessages(messageRequestClass, consumer);
    }


    public <T extends MessageResponse> MessageFuture<T> submitRequest(final String destination, final MessageRequest<T> messageRequest, Consumer<T> messageResponseConsumer) {
        final MessageFuture<T> future = this.submitRequest(destination, messageRequest);

        future.addListener(messageResponseConsumer);

        return future;
    }

    public <T extends MessageResponse> void sendMessage(final String destination, final MessageRequest<T> messageRequest) {
        this.executorService.execute(() -> {
            messageRequest.setSender(this.alias);

            this.channelHandler.getLastChannel().writeAndFlush(
                    new StringMessage(messageRequest.getMessageId(), this.alias, destination,
                            messageRequest.getClass().getName(), JsonUtil.getGsonInstance().toJson(messageRequest)));
        });
    }


    public <T extends MessageResponse> MessageFuture<T> submitRequest(final String destination, final MessageRequest<T> messageRequest) {
        final MessageFuture<T> future = new MessageFuture<T>(messageRequest);

        this.executorService.execute(() -> {
            messageRequest.setFuture(future);
            MessageRegistry.getInstance().awaitResponse(messageRequest);

            messageRequest.setSender(this.alias);

            this.channelHandler.getLastChannel().writeAndFlush(
                    new StringMessage(messageRequest.getMessageId(), this.alias, destination,
                            messageRequest.getClass().getName(), JsonUtil.getGsonInstance().toJson(messageRequest)));
        });

        return future;
    }

    public void flushData() {
        this.channelHandler.getLastChannel().flush();
    }

    public void sendResponse(UUID messageId, final String destination, final MessageResponse messageResponse) {
        this.executorService.submit(() -> {
            try {
                this.channelHandler.getLastChannel().writeAndFlush(new StringMessage(messageId, this.alias,
                        destination, messageResponse.getClass().getName(),
                        JsonUtil.getGsonInstance().toJson(messageResponse)));
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
