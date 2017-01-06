package io.coerce.networking.http.files;

public class FileData {
    private final FileType fileType;
    private final byte[] data;

    public FileData(final FileType fileType, final byte[] data) {
        this.fileType = fileType;
        this.data = data;
    }

    public FileType getFileType() {
        return this.fileType;
    }

    public byte[] getData() {
        return this.data;
    }
}
