package io.coerce.services.messaging.example.boot;

import com.google.common.collect.Maps;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.config.Configuration;
import io.coerce.services.messaging.client.MessageFuture;
import io.coerce.services.messaging.client.MessagingClient;
import io.coerce.services.messaging.client.messages.requests.types.GetAllServersRequest;
import io.coerce.services.messaging.client.messages.requests.types.GetServersByServiceNameRequest;
import io.coerce.services.messaging.client.messages.response.types.GetServersByServiceNameResponse;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataRequest;
import io.coerce.services.messaging.example.boot.messages.player.PlayerDataResponse;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PlayerDataService {
    private static final Map<Integer, String> database = Maps.newConcurrentMap();

    public static void main(String[] args) throws Exception {
        database.put(1, "Leon");
        database.put(2, "Jack");
        database.put(3, "Tony");

        final CoerceConfiguration configuration = new CoerceConfiguration();

        final MessagingClient messagingClient = MessagingClient.create(args[0], configuration);

        messagingClient.observe(PlayerDataRequest.class, (playerDataRequest -> {
            messagingClient.sendResponse(playerDataRequest.getMessageId(), playerDataRequest.getSender(),
                    new PlayerDataResponse(database.get(playerDataRequest.getPlayerId()), ""));
        }));

        messagingClient.connect("localhost", 6500, (client) -> {
//            final long totalStartTime = System.currentTimeMillis();
//
//            for(int i = 0; i < 10000; i++) {
//                final long startTime = System.currentTimeMillis();
//
//                MessageFuture<GetServersByServiceNameResponse> future = messagingClient.submitRequest("master",
//                        new GetServersByServiceNameRequest("player-service-*"), (response) -> {
//                            for (String service : response.getServices()) {
//                                System.out.println("Service discovered: " + service);
//                            }
//                        });
//                try {
//                    final GetServersByServiceNameResponse response = future.get();//
//
//                    final long timeDifference = System.currentTimeMillis() - startTime;
//
//                    System.out.println("it took " + timeDifference + "ms to get a response");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            final long totalDifference = System.currentTimeMillis() - totalStartTime;
//
//            System.out.println("It took " + totalDifference + "ms to submit & handle 10k message responses");

        });

    }
}