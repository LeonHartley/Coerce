# Coerce
Coerce is a set of Java libraries created to make high-concurrent and high-performance application development easier. It is distributed under the Apache v2 license.

## How are messages structured?
### Request Object
```java
public class PlayerDataRequest extends MessageRequest<PlayerDataResponse> {
    private final int playerId;

    public PlayerDataRequest(final int playerId) {
        super(UUID.randomUUID(), PlayerDataResponse.class);

        this.playerId = playerId;
    }

    @Override
    protected void onResponseReceived(PlayerDataResponse response) {
        System.out.println("Received user with username: " + response.getUsername());
    }

    public int getPlayerId() {
        return playerId;
    }
}
```

### Response Object
```java
public class PlayerDataResponse implements MessageResponse {
    private final String username;
    private final String email;

    public PlayerDataResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
```

## How do I request messages?
To send requests, first we need to connect to the messaging server and assign an alias to our service. Once that's done and we're fully connected, we can begin requesting messages.

```java
final MessagingClient messagingClient = MessagingClient.create("test-client", configuration);

messagingClient.connect("localhost", 8080, (client) -> {
  messagingClient.submitRequest("player-service-1", new PlayerDataRequest(1));
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


## Author
Leon Hartley <lhartley97@gmail.com>

