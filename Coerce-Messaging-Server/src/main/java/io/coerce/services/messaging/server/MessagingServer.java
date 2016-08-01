package io.coerce.services.messaging.server;

import com.google.inject.Inject;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.http.HttpServerService;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.services.CoerceService;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.messaging.server.configuration.MessagingServerConfiguration;
import io.coerce.services.messaging.server.net.MessagingChannelHandler;

public class MessagingServer extends CoerceService<MessagingServerConfiguration> {
    private final NetworkingService networkingService;

    private final Configuration configuration;
    private final MessagingChannelHandler channelHandler;
    private final HttpServerService httpServerService;

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
        this.networkingService.configure(this.channelHandler);

        this.networkingService.startService(this.getConfiguration().getHostName(),
                this.getConfiguration().getPort());

        this.httpServerService.startServer("0.0.0.0", 8081);

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/",
                (req, res) -> {
                    res.setContentType("text/html");
                    res.send("<h2>It works!</h2>");
                });

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/users/:id/:action",
                (req, res) -> {
                    res.setContentType("text/html");
                    res.send("<h2>name: " + req.getUrlParameter("id") + ", action: " + req.getUrlParameter("action") + "</h2>");
                });
    }

    @Override
    public void onServiceDispose() {

    }
}
