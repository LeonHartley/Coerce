package io.coerce.http.codec;

import io.coerce.http.types.DefaultHttpRequest;
import io.coerce.networking.channels.NetworkBuffer;
import io.coerce.networking.channels.NetworkChannel;
import io.coerce.networking.codec.ObjectDecoder;
import io.coerce.networking.http.HttpPayload;
import io.coerce.networking.http.cookies.Cookie;
import io.coerce.networking.http.requests.HttpRequestType;
import org.apache.commons.lang.StringUtils;
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

        final String[] locationString = httpRequestTypeParts[1].split("\\?");

        final String location = locationString[0];
        final String[] splitParams = locationString.length > 1 ? locationString[1].split("\\&") : null;

        final String httpVersion = httpRequestTypeParts[2];

        final Map<String, String> headers = new HashMap<>();
        final Map<String, Cookie> cookies = new HashMap<>();
        final Map<String, String> queryParameters = new HashMap<>();

        if (splitParams != null) {
            for (String splitParam : splitParams) {
                try {
                    if (splitParam.contains("=")) {
                        final String[] paramData = splitParam.split("=");
                        queryParameters.put(paramData[0], paramData.length > 1 ? URLDecoder.decode(paramData[1], "UTF-8") : "");
                    } else {
                        queryParameters.put(splitParam, "");
                    }
                } catch (Exception e) {
                    log.error("Error while parsing query parameters of request {}", StringUtils.join(locationString, "?"), e);
                }
            }
        }

        byte[] data = null;
        boolean nextLineIsData = false;

        for (int i = 1; i < requestLines.length; i++) {
            try {
                if (i == (requestLines.length - 1)) {
                    data = requestLines[i].getBytes();
                    break;
                }

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

                        final Cookie cookie = new Cookie(cookiePayload[0].trim(), URLDecoder.decode(cookiePayload[1], "UTF-8"));
                        cookies.put(cookie.getKey(), cookie);
                    }
                } else {
                    if (!header[0].isEmpty()) {
                        headers.put(header[0].trim(), header[1].trim());
                    }
                }
            } catch (Exception e) {
                log.error("Error while parsing HTTP request line {}", requestLines[i], e);
            }
        }

        return new DefaultHttpRequest(type, location, httpVersion, headers, cookies, queryParameters, data);
    }
}
