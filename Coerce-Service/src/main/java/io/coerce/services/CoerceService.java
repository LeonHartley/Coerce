package io.coerce.services;

import io.coerce.services.configuration.ServiceConfiguration;

public abstract class CoerceService<T extends ServiceConfiguration> {
    private final T configuration;

    public CoerceService(String[] runtimeArgs, T configuration) {
        this.configuration = configuration;
    }

    public abstract void onServiceInitialised();

    public abstract void onServiceDispose();

    public T getConfiguration() {
        return this.configuration;
    }
}