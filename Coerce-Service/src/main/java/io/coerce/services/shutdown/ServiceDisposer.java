package io.coerce.services.shutdown;

import com.google.inject.Singleton;
import io.coerce.services.CoerceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

@Singleton
public class ServiceDisposer {
    private final Logger log = LogManager.getLogger(ServiceDisposer.class);

    private final Queue<Consumer<ServiceDisposalReason>> disposalHooks;
    private final Queue<CoerceService> services;

    public ServiceDisposer() {
        // We use queues so we can ensure hooks are executed in the correct order.
        this.disposalHooks = new ConcurrentLinkedQueue<>();
        this.services = new ConcurrentLinkedQueue<>();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                executeHooks(ServiceDisposalReason.SHUTDOWN);
            }
        });
    }

    public void executeHooks(final ServiceDisposalReason reason) {
        log.info("Executing {} shutdown hooks", this.disposalHooks.size() + this.services.size());

        while (!this.disposalHooks.isEmpty()) {
            final Consumer<ServiceDisposalReason> disposalHook = this.disposalHooks.poll();

            disposalHook.accept(reason);
        }

        while (!this.services.isEmpty()) {
            final CoerceService service = this.services.poll();

            service.onServiceDispose();
        }
    }

    public void addService(final CoerceService service) {
        this.services.offer(service);
    }

    public void addHook(final Consumer<ServiceDisposalReason> disposalHook) {
        this.disposalHooks.offer(disposalHook);
    }
}
