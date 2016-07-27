package io.coerce.services.modules;

import com.google.inject.AbstractModule;
import io.coerce.services.ServiceBootstrap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class NetworkingModule extends AbstractModule {

    private final Map<String, String> serviceMappings;

    public NetworkingModule(Map<String, String> serviceMappings) {
        this.serviceMappings = serviceMappings;
    }

    @Override
    protected void configure() {
        for(Map.Entry<String, String> mapping : this.serviceMappings.entrySet()) {
            try {
                final Class<?> fromClass = Class.forName(mapping.getKey());
                final Class<?> toClass = Class.forName(mapping.getValue());

                bind(fromClass).to((Class) toClass);
            } catch (final Exception e) {
                System.out.println("[" + NetworkingModule.class.getName() + "] Error while parsing service mapping config");
                e.printStackTrace();
            }
        }
    }
}
