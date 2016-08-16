package io.coerce.http.codec;

import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.requests.HttpRequestType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpPayloadDecoder implements ObjectDecoder<HttpPayload> {

    private static final Logger log = LogManager.getLogger(HttpPayloadDecoder.class);

    @Override
    public HttpPayload decode(NetworkBuffer buffer, NetworkChannel channel) {
        final String requestData = buffer.toString(Charset.defaultCharset());
        final String[] requestLines = requestData.split("\r\n");

        final String[] httpRequestTypeParts = requestLines[0].split(" ");

        final HttpRequestType type = HttpRequestType.valueOf(httpRequestTypeParts[0]);
        final String location = httpRequestTypeParts[1];
        final String httpVersion = httpRequestTypeParts[2];

        final Map<String, String> headers = new HashMap<>();
        final Map<String, Cookie> cookies = new HashMap<>();

        for (int i = 1; i < requestLines.length; i++) {
            try {
                final String[] header = requestLines[i].split(":");

                if (header[0].equals("Cookie")) {
                    final String[] cookieData = header[1].split(";");

                    if (cookieData.length < 1) {
                        continue;
                    }

                    if (cookieData.length >= 1024) {
                        // max cookie length, we don't wanna try to parse this
                        continue;
                    }

                    for (String cookieEntry : cookieData) {
                        if (!cookieEntry.contains("=")) {
                            continue;
                        }

                        final String[] cookiePayload = cookieEntry.split("=");

                        final Cookie cookie = new Cookie(cookiePayload[0], URLDecoder.decode(cookiePayload[1], "UTF-8"));

                        if (cookies.containsKey(cookie.getKey())) {
                            cookies.replace(cookie.getKey(), cookie);
                        } else {
                            cookies.put(cookie.getKey(), cookie);
                        }
                    }
                } else {
                    headers.put(header[0].trim(), header[1].trim());
                }
            } catch(Exception e) {
                log.error("Error while parsing HTTP request line {}", requestLines[i], e);
            }
        }

        return new DefaultHttpRequest(type, location, httpVersion, headers, cookies);
    }
}
