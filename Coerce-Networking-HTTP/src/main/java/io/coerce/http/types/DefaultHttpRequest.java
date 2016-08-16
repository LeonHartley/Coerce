package io.coerce.http.types;

import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.http.cookies.Cookie;
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
    private final Map<String, Cookie> cookies;
    private final byte[] requestData;

    private NetworkChannel networkChannel;
    private ViewParser viewParser;

    private Route route;

    private HttpSession session;

    public DefaultHttpRequest(HttpRequestType type, String location, String httpVersion,
                              Map<String, String> headers, Map<String, Cookie> cookies, byte[] requestData) {
        this.type = type;
        this.location = location;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.requestData = requestData;
        this.cookies = cookies;
    }

    public DefaultHttpRequest(HttpRequestType type, String location, String httpVersion,
                              Map<String, String> headers, Map<String, Cookie> cookies) {
        this(type, location, httpVersion, headers, cookies, null);
    }

    @Override
    public HttpRequestType getType() {
        return this.type;
    }

    @Override
    public HttpSession getSession() {
        return this.session;
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

    @Override
    public Map<String, Cookie> getCookies() {
        return this.cookies;
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

    public void setSession(final HttpSession httpSession) {
        this.session = httpSession;
    }
}
