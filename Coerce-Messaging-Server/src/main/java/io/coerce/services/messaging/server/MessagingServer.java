package io.coerce.services.messaging.server;

import com.google.inject.Inject;
import io.coerce.commons.config.Configuration;
import io.coerce.networking.NetworkingService;
import io.coerce.services.CoerceService;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.messaging.server.configuration.MessagingServerConfiguration;
import io.coerce.services.messaging.server.net.MessagingChannelHandler;

public class MessagingServer extends CoerceService<MessagingServerConfiguration> {
    private final NetworkingService networkingService;
    private final Configuration configuration;
    private final MessagingChannelHandler channelHandler;

    @Inject
    public MessagingServer(String[] runtimeArgs, ServiceConfiguration serviceConfiguration,
                           NetworkingService networkingService, Configuration configuration,
                           MessagingChannelHandler channelHandler) {
        super(runtimeArgs, (MessagingServerConfiguration) serviceConfiguration);

        this.networkingService = networkingService;
        this.configuration = configuration;
        this.channelHandler = channelHandler;
    }

    @Override
    public void onServiceInitialised() {
        this.networkingService.configure(this.channelHandler);

        this.networkingService.startService(this.getConfiguration().getHostName(),
                this.getConfiguration().getPort());
    }

    @Override
    public void onServiceDispose() {

    }
}
