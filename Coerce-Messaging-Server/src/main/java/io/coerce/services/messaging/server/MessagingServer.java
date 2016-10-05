package io.coerce.services.messaging.server;

import com.google.inject.Inject;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.http.HttpServerService;
import io.coerce.persistence.dao.DaoProvider;
import io.coerce.services.CoerceService;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.types.GetAllServersRequest;
import io.coerce.services.messaging.client.messages.requests.types.GetServersByServiceNameRequest;
import io.coerce.services.messaging.server.configuration.MessagingServerConfiguration;
import io.coerce.services.messaging.server.messages.MessageHandler;
import io.coerce.services.messaging.server.net.MessagingChannelHandler;
import io.coerce.services.messaging.server.persistence.LogDao;
import io.coerce.services.messaging.server.sessions.SessionManager;
import io.coerce.services.messaging.server.web.MessagingWebInterface;

public class MessagingServer extends CoerceService<MessagingServerConfiguration> {
    private final NetworkingService networkingService;

    private final MessagingChannelHandler channelHandler;
    private final HttpServerService httpServerService;
    private final MessagingClient messagingClient;
    private final SessionManager sessionManager;

    private MessagingWebInterface webInterface;

    @Inject
    public MessagingServer(String[] runtimeArgs, ServiceConfiguration serviceConfiguration,
                           NetworkingService networkingService, CoerceConfiguration configuration,
                           MessagingChannelHandler channelHandler, HttpServerService httpServer,
                           SessionManager sessionManager, DaoProvider daoProvider) {
        super(runtimeArgs, (MessagingServerConfiguration) serviceConfiguration);

        this.networkingService = networkingService;
        this.channelHandler = channelHandler;
        this.httpServerService = httpServer;
        this.sessionManager = sessionManager;

        final LogDao logDao = daoProvider.getDao(LogDao.class);

        logDao.create("test", "just a test log");

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
                                    (request) -> MessageHandler.getAllServers(messagingClient, request, sessionManager));

                            client.observe(GetServersByServiceNameRequest.class,
                                    (request) -> MessageHandler.getAllServersByName(messagingClient, request, sessionManager));
                        }));

        this.webInterface = new MessagingWebInterface(this.httpServerService.getRoutingService(), this.sessionManager);
        this.webInterface.initialiseRoutes();

        this.httpServerService.startServer("0.0.0.0", 8081, (server) -> {

        });
    }

    @Override
    public void onServiceDispose() {

    }
}
