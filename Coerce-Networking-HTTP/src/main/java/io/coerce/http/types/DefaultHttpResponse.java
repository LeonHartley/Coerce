package io.coerce.http.types;

import com.google.gson.JsonObject;
import io.coerce.commons.json.JsonUtil;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.networking.http.responses.HttpResponseCode;
import io.coerce.networking.http.responses.views.ViewParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultHttpResponse implements HttpResponse {
    private static final Logger log = LogManager.getLogger(DefaultHttpResponse.class);
    private static final String HTTP_VERSION = "HTTP/1.1";

    private final NetworkChannel networkChannel;

    private HttpResponseCode responseType = HttpResponseCode.OK;
    private String contentType = "text/plain";

    private final Map<String, String> headers;
    private final Map<String, Cookie> cookies;
    private final ViewParser viewParser;

    public DefaultHttpResponse(ViewParser viewParser, NetworkChannel networkChannel) {
        this.viewParser = viewParser;
        this.networkChannel = networkChannel;

        this.headers = new ConcurrentHashMap<>();
        this.cookies = new ConcurrentHashMap<>();
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void send(final byte[] bytes) {
        this.headers.put("Content-Length", "" + bytes.length);
        this.headers.put("Content-Type", this.getContentType());

        if (this.networkChannel == null) {
            return;
        }

        if(!this.cookies.isEmpty()) {
            try {
            String setCookieHeader = "";

            for(Cookie cookie : this.cookies.values()) {
                setCookieHeader += cookie.getHeader();
            }

            this.headers.put("Set-Cookie", setCookieHeader);
            } catch (Exception e) {
                log.error("Error while encoding cookie data", e);
            }
        }

        // build the http payload & send it
        this.networkChannel.writeAndClose(new ResponsePayload(
                HTTP_VERSION + " " + this.responseType.getResponseCode() + " " + this.responseType.getResponse(),
                this.headers,
                bytes
        ));
    }

    @Override
    public void send(final String string) {
        this.send(string.getBytes());
    }

    @Override
    public void renderView(String view, Map<String, Object> model) {
        // build the view along with the model then we build the payload then send it.
        this.contentType = "text/html";
        try {
            this.send(this.viewParser.render(view, model));
        } catch (Exception e) {
            // error 500
            this.setResponseCode(HttpResponseCode.INTERNAL_ERROR);
            this.setContentType("text/plain");

            // TODO: Only send the exception information when the server is running in development mode.
            this.send("Internal server error\n\n" + e.getMessage());
        }
    }

    @Override
    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public String getHeader(String key) {
        return this.headers.get(key);
    }

    @Override
    public void setCookie(Cookie cookie) {
        this.cookies.put(cookie.getKey(), cookie);
    }

    @Override
    public boolean hasCookie(String key) {
        return this.cookies.containsKey(key);
    }

    @Override
    public HttpResponseCode getResponseCode() {
        return this.responseType;
    }

    @Override
    public void setResponseCode(HttpResponseCode responseType) {
        this.responseType = responseType;
    }

    @Override
    public void redirect(String location) {
        this.setResponseCode(HttpResponseCode.MOVED);
        this.setHeader("Location", "/");
        this.send("");
    }

    private class ResponsePayload implements HttpPayload {

        private final String header;
        private final Map<String, String> headers;
        private final byte[] data;

        public ResponsePayload(final String header, Map<String, String> headers, byte[] data) {
            this.header = header;
            this.headers = headers;
            this.data = data;
        }

        @Override
        public String getHeader() {
            return this.header;
        }

        @Override
        public Map<String, String> getHeaders() {
            return this.headers;
        }

        @Override
        public byte[] getData() {
            return this.data;
        }

        @Override
        public JsonObject getDataAsJson() {
            return null;
        }
    }
}
