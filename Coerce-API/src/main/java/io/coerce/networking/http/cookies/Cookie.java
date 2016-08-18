package io.coerce.networking.http.cookies;

public class Cookie {

    private final String key;
    private final String value;
    private final String expiry;

    public Cookie(final String key, final String value, final String expiry) {
        this.key = key;
        this.value = value;
        this.expiry = expiry;
    }

    public Cookie(final String key, final String value) {
        this(key, value, null);
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return this.value;
    }

    public String getExpiry() {
        return this.expiry;
    }

    public String getHeader() {
        return this.key + "=" + this.value + "; " +
                (this.expiry != null ? "Expires=" + this.expiry : "");
    }
}
