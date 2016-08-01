package io.coerce.networking.http.responses.views;

import java.util.Map;

public interface ViewParser {
    String render(final String view, final Map<String, Object> model);
}
