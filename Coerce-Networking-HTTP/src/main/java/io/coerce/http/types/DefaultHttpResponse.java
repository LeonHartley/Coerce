package io.coerce.http.types;

import io.coerce.http.server.responses.ThymeleafViewParser;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.responses.HttpResponse;
import io.coerce.networking.http.responses.HttpResponseCode;
import io.coerce.networking.http.responses.views.ViewParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultHttpResponse implements HttpResponse {
    private static final String HTTP_VERSION = "HTTP/1.1";

    private final NetworkChannel networkChannel;

    private HttpResponseCode responseType = HttpResponseCode.OK;
    private String contentType = "text/plain";

    private final Map<String, String> headers;
    private final ViewParser viewParser;

    public DefaultHttpResponse(ViewParser viewParser, NetworkChannel networkChannel) {
        this.viewParser = viewParser;
        this.networkChannel = networkChannel;
        this.headers = new ConcurrentHashMap<>();
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
    public void send(String string) {
        final byte[] data = string.getBytes();

        this.headers.put("Content-Length", "" + data.length);
        this.headers.put("Content-Type", this.getContentType());

        if(this.networkChannel == null) {
            return;
        }

        // build the http payload & send it
        this.networkChannel.writeAndClose(new ResponsePayload(
                HTTP_VERSION + " " + this.responseType.getResponseCode() + " " + this.responseType.getResponse(),
                this.headers,
                string.getBytes()
        ));
    }

    @Override
    public void renderView(String view, Map<String, Object> model) {
        // build the view along with the model then we build the payload then send it.
        this.send(this.viewParser.render(view, model));
    }

    @Override
    public HttpResponseCode getResponseCode() {
        return this.responseType;
    }

    @Override
    public void setResponseCode(HttpResponseCode responseType) {
        this.responseType = responseType;
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
    }
}
