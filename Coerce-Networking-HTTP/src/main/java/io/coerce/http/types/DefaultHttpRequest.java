package io.coerce.http.types;

import com.google.gson.JsonObject;
import io.coerce.commons.json.JsonUtil;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.requests.HttpRequest;
import io.coerce.networking.http.requests.HttpRequestType;
import io.coerce.networking.http.responses.views.ViewParser;
import io.coerce.networking.http.sessions.HttpSession;
import org.apache.commons.lang.CharUtils;
import org.bigtesting.routd.Route;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class DefaultHttpRequest implements HttpRequest {
    private final String httpVersion;

    private final HttpRequestType type;
    private final String location;

    private final Map<String, String> headers;
    private final Map<String, Cookie> cookies;
    private final Map<String, String> queryParameters;

    private final byte[] requestData;

    private JsonObject requestDataJson;

    private NetworkChannel networkChannel;
    private ViewParser viewParser;

    private Route route;

    private HttpSession session;

    private Map<String, String> formData;

    public DefaultHttpRequest(HttpRequestType type, String location, String httpVersion,
                              Map<String, String> headers, Map<String, Cookie> cookies,
                              Map<String, String> queryParameters, byte[] requestData) {
        this.type = type;
        this.location = location;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.cookies = cookies;
        this.queryParameters = queryParameters;
        this.requestData = requestData;
    }

    public DefaultHttpRequest(HttpRequestType type, String location, String httpVersion,
                              Map<String, String> headers, Map<String, Cookie> cookies,
                              Map<String, String> queryParameters) {
        this(type, location, httpVersion, headers, cookies, queryParameters, null);
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
    public JsonObject getDataAsJson() {
        if(this.requestDataJson == null) {
            this.requestDataJson = JsonUtil.getGsonInstance().fromJson(
                    new String(this.requestData), JsonObject.class);
        }

        return this.requestDataJson;
    }

    @Override
    public String getUrlParameter(final String key) {
        if(key.equals("*")) {

            return this.route.getSplatParameter(0, this.getLocation());
        }

        return this.route.getNamedParameter(key, this.getLocation());
    }

    @Override
    public String getQueryParameter(String key) {
        return this.queryParameters.get(key);
    }

    @Override
    public boolean hasQueryParameter(String key) {
        return this.queryParameters.containsKey(key);
    }

    @Override
    public Map<String, String> getFormData() {
        if(this.formData == null && this.requestData.length > 1) {
            this.formData = new HashMap<>();
        }

        try {
            final String[] splitData = new String(this.requestData, "UTF-8").split("&");

            for(String formField : splitData) {
                final String[] splitField = formField.split("=");

                final String key = URLDecoder.decode(splitField[0], "UTF-8");
                final String value = splitField.length == 2 ? URLDecoder.decode(splitField[1], "UTF-8") : null;

                this.formData.put(key, value);
            }
        } catch (Exception e) {
            // TODO: logging pipeline
            e.printStackTrace();
            return this.formData;
        }

        return this.formData;
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
