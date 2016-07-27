# Coerce
Soon-to-be open source repository of the Coerce java libraries.

## How do I request messages?
To send requests, first we need to connect to the messaging server, assign an alias to our service. Once that's done and we're fully connected, we can begin requesting messages.

```java
this.messagingClient = MessagingClient.create("test-client", configuration);

this.messagingClient.connect("localhost", 8080, (client) -> {
  this.messagingClient.submitRequest("player-service-1", new PlayerDataRequest(1));
});
```

## How do I listen for message requests?
The following code is a full example that shows how we begin observing for messages based on the class.

```java
final MessagingClient messagingClient = MessagingClient.create("player-service-1", configuration);

messagingClient.observe(PlayerDataRequest.class, (playerDataRequest -> {
  messagingClient.sendResponse(playerDataRequest.getMessageId(), playerDataRequest.getSender(),
    new PlayerDataResponse(database.get(playerDataRequest.getPlayerId()), ""));
}));

messagingClient.connect("localhost", 8080, (client) -> {
  // We don't need to do anything here as we aren't sending any messages.
});
```
