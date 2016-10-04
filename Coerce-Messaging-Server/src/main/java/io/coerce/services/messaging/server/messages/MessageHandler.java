package io.coerce.services.messaging.server.messages;

import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.types.GetAllServersRequest;
import io.coerce.services.messaging.client.messages.requests.types.GetServersByServiceNameRequest;
import io.coerce.services.messaging.client.messages.response.types.GetAllServersResponse;
import io.coerce.services.messaging.client.messages.response.types.GetServersByServiceNameResponse;
import io.coerce.services.messaging.server.sessions.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    public static void getAllServers(final MessagingClient client, final GetAllServersRequest request,
                                     final SessionManager sessionManager) {
        final List<String> services = new ArrayList<>();

        for (String alias : sessionManager.getSessions().keySet()) {
            services.add(alias);
        }

        client.sendResponse(request.getMessageId(), request.getSender(), new GetAllServersResponse(services));
    }

    public static void getAllServersByName(final MessagingClient client, final GetServersByServiceNameRequest request,
                                           SessionManager sessionManager) {
        final List<String> services = new ArrayList<>();

        final String searchString = request.getNamePattern().replace("*", "");

        for (String alias : sessionManager.getSessions().keySet()) {
            if(alias.startsWith(searchString)) {
                services.add(alias);
            }
        }

        client.sendResponse(request.getMessageId(), request.getSender(), new GetServersByServiceNameResponse(services));
    }
}
