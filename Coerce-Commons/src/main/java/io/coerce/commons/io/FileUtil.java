package io.coerce.commons.io;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FileUtil {
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    public static byte[] loadFile(final String fileLocation) {
        try {
            final File file = new File(fileLocation);
            final byte[] fileContents = Files.readAllBytes(file.toPath());

            return fileContents;
        } catch (Exception e) {
            return null;
        }
    }
}
