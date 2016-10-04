package io.coerce.services.modules;

import com.google.inject.AbstractModule;
import io.coerce.commons.config.CoerceConfiguration;
import io.coerce.commons.config.Configuration;
import io.coerce.services.configuration.ServiceConfiguration;

public class StartupModule extends AbstractModule {

    private final String[] args;
    private final ServiceConfiguration serviceConfiguration;

    public StartupModule(final String[] args, final ServiceConfiguration serviceConfiguration) {
        this.args = args;
        this.serviceConfiguration = serviceConfiguration;
    }

    @Override
    protected void configure() {
        bind(String[].class).toInstance(this.args);
        bind(ServiceConfiguration.class).toInstance(this.serviceConfiguration);
        bind(CoerceConfiguration.class).toInstance(new CoerceConfiguration());
    }
}
