package io.coerce.networking.http.files;

public enum FileType {
    HTML("text/html"),
    JAVASCRIPT("application/javascript"),
    CSS("text/css"),
    PNG("image/png"),
    GIF("image/gif"),
    JPG("image/jpg"),
    TXT("text/plain"),
    UNKNOWN("application/octet-stream");

    private final String contentType;

    FileType(final String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return this.contentType;
    }
}
