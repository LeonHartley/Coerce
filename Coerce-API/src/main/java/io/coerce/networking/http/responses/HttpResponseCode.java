package io.coerce.networking.http.responses;

public enum HttpResponseCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    FORBIDDEN(403, "Forbidden"),
    INTERNAL_ERROR(500, "Internal Server Error"),
    MOVED(301, "Moved Permanently");

    private final int responseCode;
    private final String response;

    HttpResponseCode(final int responseCode, final String response) {
        this.responseCode = responseCode;
        this.response = response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponse() {
        return response;
    }
}
