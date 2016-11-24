# Coerce
Coerce is a set of Java libraries created to make high-concurrent and high-performance application development easier. It is distributed under the Apache v2 license.

## Messaging
Coerce Messaging is a library and a server created to make an extremely simple microservice architecture with little code and configuration.

### How are messages structured?
#### Request Object
```java
public class PlayerDataRequest extends MessageRequest<PlayerDataResponse> {
    private final int playerId;

    public PlayerDataRequest(final int playerId) {
        super(UUID.randomUUID(), PlayerDataResponse.class);

        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
```

#### Response Object
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

### How do I request messages?
To send requests, first we need to connect to the messaging server and assign an alias to our service. Once that's done and we're fully connected, we can begin requesting messages.

```java
final MessagingClient messagingClient = MessagingClient.create("test-client", configuration);

messagingClient.connect("localhost", 8080, (client) -> {
  final MessageFuture<PlayerDataResponse> playerDataFuture = messagingClient.submitRequest("player-service-1", 
        new PlayerDataRequest(1));
  
  // We can add a completion handler by either passing a 3rd argument to the above submitRequest call (a consumer)
  // or we can add one directly to the future
  playerDataFuture.addListener((playerData) -> {
      System.out.println("Received user with username: " + playerData.getUsername());
  });
  
  // Or we can block the current thread and wait for a response! 
  final PlayerDataResponse playerData = playerDataFuture.get();
  System.out.println("Received user with username: " + playerData.getUsername());
});
```

### How do I listen for message requests?
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

## HTTP Server
Coerce has a built-in HTTP framework, allowing you to create web services without the need for the amount of initialisation you usually come to expect with building websites in Java. Coerce makes this really simple:

_This example presumes you are using Coerce-Service and are injecting a HttpServerService instance into your application._

```java
// Initialise the routes
this.httpServerService.getRoutingService().addRoute(HttpRequestType.GET, "/", (req, res) -> {
    res.send("hello world!");    
});

// Start the server
this.httpServerService.start("127.0.0.1", 8080);
```

Once this is running, visiting "http://127.0.0.1:8080" will output "hello world!"! 

## Service
The Coerce-Service module was created to remove the need to write boilerplate code to initialise components like configuration and also provides easy access to Guice for dependency injection.

## Author
Leon Hartley (<lhartley97@gmail.com>)

