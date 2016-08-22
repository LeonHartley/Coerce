package io.coerce.services.messaging.server;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.networking.http.HttpServerService;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.networking.http.sessions.SessionObjectKey;
import io.coerce.services.CoerceService;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.messaging.server.configuration.MessagingServerConfiguration;
import io.coerce.services.messaging.server.net.MessagingChannelHandler;
import io.coerce.services.messaging.server.web.MessagingWebInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.BiConsumer;

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

    private static byte[] data;

    @Override
    public void onServiceInitialised() {
        this.networkingService.initialise(this.channelHandler);

        this.networkingService.startService(this.getConfiguration().getHostName(),
                this.getConfiguration().getPort());

        this.httpServerService.startServer("0.0.0.0", 8081);

        this.webInterface = new MessagingWebInterface(this.httpServerService.getRoutingService());
        this.webInterface.initialiseRoutes();

        final SessionObjectKey<String> username = new SessionObjectKey<>("Username");

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/session/:username", (request, httpResponse) -> {
                request.getSession().setObject(username, request.getUrlParameter("username"));

                httpResponse.send("session data set successfully");
        });

        this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/", (req, res) -> {
            if(req.getSession().getObject(username) == null) {
                res.send("no username set");
                return;
            }

            res.send(req.getSession().getObject(username));
        });

        try {
            File imgPath = new File("configuration/telme.png");
            data = Files.readAllBytes(imgPath.toPath());

            this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/images/telme.png", (req, res) -> {
                res.setContentType("image/png");

                res.send(data);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDispose() {

    }
}
