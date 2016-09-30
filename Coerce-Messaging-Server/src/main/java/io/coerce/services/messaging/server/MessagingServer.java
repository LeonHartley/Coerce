package io.coerce.services.messaging.server;

import com.google.inject.Inject;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.http.HttpServerService;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.services.CoerceService;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.types.GetAllServersRequest;
import io.coerce.services.messaging.client.messages.requests.types.GetServersByServiceNameRequest;
import io.coerce.services.messaging.server.configuration.MessagingServerConfiguration;
import io.coerce.services.messaging.server.messages.MessageHandler;
import io.coerce.services.messaging.server.net.MessagingChannelHandler;
import io.coerce.services.messaging.server.sessions.SessionManager;
import io.coerce.services.messaging.server.web.MessagingWebInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class MessagingServer extends CoerceService<MessagingServerConfiguration> {
    private final NetworkingService networkingService;

    private final Configuration configuration;
    private final MessagingChannelHandler channelHandler;
    private final HttpServerService httpServerService;
    private final MessagingClient messagingClient;
    private final MessageHandler messageHandler;

    private MessagingWebInterface webInterface;

    @Inject
    public MessagingServer(String[] runtimeArgs, ServiceConfiguration serviceConfiguration,
                           NetworkingService networkingService, Configuration configuration,
                           MessagingChannelHandler channelHandler, HttpServerService httpServer, MessageHandler messageHandler) {
        super(runtimeArgs, (MessagingServerConfiguration) serviceConfiguration);

        this.networkingService = networkingService;
        this.configuration = configuration;
        this.channelHandler = channelHandler;
        this.httpServerService = httpServer;
        this.messageHandler = messageHandler;

        this.messagingClient = MessagingClient.create("master", configuration);
    }

    @Override
    public void onServiceInitialised() {
        this.networkingService.initialise(this.channelHandler);

        this.networkingService.startService(this.getConfiguration().getHostName(),
                this.getConfiguration().getPort(), (server) -> this.messagingClient.connect(
                        this.getConfiguration().getHostName(),
                        this.getConfiguration().getPort(),
                        (client) -> {
                            client.observe(GetAllServersRequest.class,
                                    (request) -> MessageHandler.getAllServers(messagingClient, request));

                            client.observe(GetServersByServiceNameRequest.class,
                                    (request) -> MessageHandler.getAllServersByName(messagingClient, request));
                        }));

        this.httpServerService.startServer("0.0.0.0", 8081);

        this.webInterface = new MessagingWebInterface(this.httpServerService.getRoutingService());
        this.webInterface.initialiseRoutes();

        /*final SessionObjectKey<String> username = new SessionObjectKey<>("Username");

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/session/:username", (request, httpResponse) -> {
            request.getSession().setObject(username, request.getUrlParameter("username"));

            httpResponse.send("session data set successfully");
        });*/

        BiConsumer<HttpRequest, HttpResponse> consumer = (req, res) -> {
            final Map<String, Object> model = new HashMap<>();
            final List<String> services = new ArrayList<>();

            for (String alias : SessionManager.getInstance().getSessions().keySet()) {
                services.add(alias);
            }

            model.put("services", services);
            res.renderView("index.messaging", model);
        };

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/", consumer);
    }

    @Override
    public void onServiceDispose() {

    }
}
