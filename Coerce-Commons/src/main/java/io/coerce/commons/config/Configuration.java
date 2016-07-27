package io.coerce.commons.config;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.coerce.commons.io.FileUtil;
import io.coerce.commons.json.JsonUtil;

@Singleton
public class Configuration {
    private final JsonObject config;

    public Configuration() {
        this.config = JsonUtil.getGsonInstance().fromJson(FileUtil.loadFile("configuration/Configuration.json"),
                JsonObject.class).getAsJsonObject("config");
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
