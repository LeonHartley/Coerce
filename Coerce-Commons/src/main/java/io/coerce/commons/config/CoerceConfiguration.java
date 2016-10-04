package io.coerce.commons.config;

import com.google.inject.Singleton;

@Singleton
public class CoerceConfiguration extends Configuration {

    public CoerceConfiguration() {
        super("configuration/Coerce.json");
    }
}
