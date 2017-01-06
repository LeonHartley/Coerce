package io.coerce.http.files;

import io.coerce.commons.io.FileUtil;
import io.coerce.networking.http.files.FileData;
import io.coerce.networking.http.files.FileProvider;
import io.coerce.networking.http.files.FileType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultCachedFileProvider implements FileProvider {

    private final Logger log = LogManager.getLogger(DefaultCachedFileProvider.class);
    private final Map<String, FileData> cachedFiles;

    public DefaultCachedFileProvider() {
        this.cachedFiles = new ConcurrentHashMap<>();
    }

    @Override
    public FileData getFile(String fileName) {
        if (this.cachedFiles.containsKey(fileName)) {
            final FileData fileData = this.cachedFiles.get(fileName);

            if(fileData.getFileType() == null || fileData.getData() == null) {
                return null;
            }

            return fileData;
        }

        final byte[] fileData = FileUtil.loadFile(fileName);

        if(fileData == null) {
            // cache that it's null so hitting 404's constantly doesn't degrade performance
            this.cachedFiles.put(fileName, new FileData(null, null));
            return null;
        }

        final String[] splitName = fileName.split("\\.");
        final String extension = splitName[splitName.length - 1];

        FileType fileType = null;

        try {
            fileType = FileType.valueOf(extension.toUpperCase());
        } catch (Exception e) {
            fileType = FileType.UNKNOWN;
        }

        final FileData obj = new FileData(fileType, fileData);

        this.cachedFiles.put(fileName, obj);
        return obj;
    }
}
