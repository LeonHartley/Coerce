package io.coerce.commons.config;

import com.google.gson.JsonObject;
import com.google.inject.Singleton;
import io.coerce.commons.io.FileUtil;
import io.coerce.commons.json.JsonUtil;

import java.io.UnsupportedEncodingException;

public class Configuration {
    private final JsonObject config;

    public Configuration(final String configurationFile) throws Exception {
        final byte[] data = FileUtil.loadFile(configurationFile);

        if(data == null) {
            throw new Exception("Failed to load configuration, file not found");
        }

        final String fileContents = new String(data, "UTF-8");

        this.config = JsonUtil.getGsonInstance().fromJson(fileContents,
                JsonObject.class);
    }

    public Configuration(JsonObject jsonObject) {
        this.config = jsonObject;
    }

    public String getString(final String key) {
        return this.config.get(key).getAsString();
    }

    public int getInt(final String key) {
        return this.config.get(key).getAsInt();
    }

    public boolean getBoolean(final String key) {
        return this.config.get(key).getAsBoolean();
    }

    public Configuration getObject(final String key) {
        return new Configuration(this.config.getAsJsonObject(key));
    }
}
