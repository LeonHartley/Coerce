package io.coerce.services.messaging.server;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.http.HttpServerService;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.services.CoerceService;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.messaging.server.configuration.MessagingServerConfiguration;
import io.coerce.services.messaging.server.net.MessagingChannelHandler;
import io.coerce.services.messaging.server.web.MessagingWebInterface;

import java.util.Map;

public class MessagingServer extends CoerceService<MessagingServerConfiguration> {
    private final NetworkingService networkingService;

    private final Configuration configuration;
    private final MessagingChannelHandler channelHandler;
    private final HttpServerService httpServerService;

    private MessagingWebInterface webInterface;


    @Inject
    public MessagingServer(String[] runtimeArgs, ServiceConfiguration serviceConfiguration,
                           NetworkingService networkingService, Configuration configuration,
                           MessagingChannelHandler channelHandler, HttpServerService httpServer) {
        super(runtimeArgs, (MessagingServerConfiguration) serviceConfiguration);

        this.networkingService = networkingService;
        this.configuration = configuration;
        this.channelHandler = channelHandler;
        this.httpServerService = httpServer;
    }

    @Override
    public void onServiceInitialised() {
        this.networkingService.initialise(this.channelHandler);

        this.networkingService.startService(this.getConfiguration().getHostName(),
                this.getConfiguration().getPort());

        this.httpServerService.startServer("0.0.0.0", 8081);

        this.webInterface = new MessagingWebInterface(this.httpServerService.getRoutingService());
        this.webInterface.initialiseRoutes();

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/",
                (req, res) -> {
                    final Map<String, Object> model = Maps.newHashMap();

                    model.put("hi", "hello!!");

                    res.setContentType("text/html");
                    res.renderView("index.messaging", model);
                });
    }

    @Override
    public void onServiceDispose() {

    }
}
