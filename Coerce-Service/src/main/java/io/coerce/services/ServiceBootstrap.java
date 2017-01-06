package io.coerce.services;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.coerce.commons.io.FileUtil;
import io.coerce.commons.json.JsonUtil;
import io.coerce.services.configuration.ServiceConfiguration;
import io.coerce.services.modules.ModuleMap;
import io.coerce.services.modules.NetworkingModule;
import io.coerce.services.modules.StartupModule;
import io.coerce.services.shutdown.ServiceDisposer;

import java.io.UnsupportedEncodingException;

public class ServiceBootstrap {
    public static <T extends CoerceService> T startService(Class<T> serviceClass, String[] args) {
        final ServiceConfiguration configuration = loadServiceConfiguration(serviceClass);

        if (configuration == null) {
            return null;
        }

        configureLogging();

        final Injector injector = createServiceInjector(args, configuration);

        try {
            final T service = injector.getInstance(serviceClass);

            // Initialise any shutdown hooks
            final ServiceDisposer serviceDisposer = injector.getInstance(ServiceDisposer.class);
            serviceDisposer.addService(service);

            // Submit this to a thread pool?
            service.onServiceInitialised();

            return service;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Injector createServiceInjector(String[] args, final ServiceConfiguration configuration) {
        ModuleMap moduleMap = null;

        try {
            moduleMap = JsonUtil.getGsonInstance().fromJson(
                    new String(FileUtil.loadFile("configuration/Coerce.json"), "UTF-8"), ModuleMap.class);
        } catch (Exception e) {
            return null;
        }

        return moduleMap == null ? null : Guice.createInjector(
                new StartupModule(args, configuration),
                new NetworkingModule(moduleMap.getModules().get("networking"))
                // Any other modules we might want to add.
        );
    }

    @SuppressWarnings("unchecked")
    private static ServiceConfiguration loadServiceConfiguration(Class<? extends CoerceService> serviceClass) {
        try {
            final Class<? extends ServiceConfiguration> configClass =
                    (Class<? extends ServiceConfiguration>) Class.forName(
                            serviceClass.getGenericSuperclass().getTypeName().replace(">", "").split("<")[1]);

            final String configFileLocation = "configuration/" + serviceClass.getSimpleName() + ".json";

            final String configData = new String(FileUtil.loadFile(configFileLocation), "UTF-8");
            return JsonUtil.getGsonInstance().fromJson(configData, configClass);
        } catch (Exception e) {
            System.out.println("[" + ServiceBootstrap.class.getName() + "] " +
                    "Failed to initialise service configuration for service " + serviceClass.getName());
            e.printStackTrace();
        }

        return null;
    }

    private static void configureLogging() {
        System.setProperty("log4j.configurationFile", "configuration/log4j2.xml");
    }
}
