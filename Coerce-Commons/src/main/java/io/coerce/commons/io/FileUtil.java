package io.coerce.commons.io;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.io.File;
import java.nio.file.Files;

public class FileUtil {
    private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();

    public static String loadFile(final String fileLocation) {
        try {
            final File file = new File(fileLocation);
            final String fileContents = new String(Files.readAllBytes(file.toPath()));

            if(fileContents.isEmpty()) {
                System.out.println(String.format("[%s] File loaded from %s was empty", FileUtil.class.getName(), fileLocation));
            }

            return fileContents;
        } catch(Exception e) {
            System.out.println(String.format("[%s] Failed to load file %s", FileUtil.class.getName(), fileLocation));
            e.printStackTrace();
        }

        return null;
    }
}
