package io.coerce.commons.config;

import com.google.inject.Singleton;

@Singleton
public class CoerceConfiguration extends Configuration {

    public CoerceConfiguration(final String configLocation) throws Exception {
        super(configLocation);

    }

    public CoerceConfiguration() throws Exception {
        super("configuration/Coerce.json");
    }
}
