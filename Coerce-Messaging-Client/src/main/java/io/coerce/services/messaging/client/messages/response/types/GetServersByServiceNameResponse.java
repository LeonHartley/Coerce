package io.coerce.services.messaging.client.messages.response.types;

import java.util.List;

public class GetServersByServiceNameResponse extends GetAllServersResponse {
    public GetServersByServiceNameResponse(List<String> services) {
        super(services);
    }
}
