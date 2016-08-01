package io.coerce.http.types;

import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.responses.views.ViewParser;
import io.coerce.networking.http.sessions.HttpSession;
import org.bigtesting.routd.Route;

import java.util.Map;

public class DefaultHttpRequest implements HttpRequest {
    private final String httpVersion;

    private final HttpRequestType type;
    private final String location;

    private final Map<String, String> headers;
    private final byte[] requestData;

    private NetworkChannel networkChannel;
    private ViewParser viewParser;

    private Route route;

    public DefaultHttpRequest(HttpRequestType type, String location, String httpVersion, Map<String, String> headers, byte[] requestData) {

        this.type = type;
        this.location = location;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.requestData = requestData;
    }

    public DefaultHttpRequest(HttpRequestType type, String location, String httpVersion, Map<String, String> headers) {
        this(type, location, httpVersion, headers, null);
    }

    @Override
    public HttpRequestType getType() {
        return this.type;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String getLocation() {
        return this.location;
    }

    @Override
    public String getHttpVersion() {
        return this.httpVersion;
    }

    @Override
    public String getHeader() {
        return type.toString() + " " + this.location + " " + this.httpVersion;
    }

    @Override
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    @Override
    public byte[] getData() {
        return requestData;
    }

    @Override
    public String getUrlParameter(final String key) {
        return this.route.getNamedParameter(key, this.getLocation());
    }

    public void setRoute(final Route route) {
        this.route = route;
    }

    public NetworkChannel getNetworkChannel() {
        return networkChannel;
    }

    public void setNetworkChannel(final NetworkChannel networkChannel) {
        this.networkChannel = networkChannel;
    }

    public ViewParser getViewParser() {
        return this.viewParser;
    }

    public void setViewParser(final ViewParser viewParser) {
        this.viewParser = viewParser;
    }
}
