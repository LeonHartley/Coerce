package io.coerce.networking.nio.events;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.channels.spi.AbstractSelectableChannel;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NioEventExecutorGroup {
    private final int poolSize = 2;

    private final Logger log = LogManager.getLogger(NioEventExecutorGroup.class);

    private List<NioEventExecutor> eventExecutors;
    private final Provider<NioEventExecutor> executorProvider;

    @Inject
    public NioEventExecutorGroup(Provider<NioEventExecutor> executorProvider) throws Exception {
        this.executorProvider = executorProvider;
        this.buildGroup();
    }

    private void buildGroup() throws Exception {
        this.eventExecutors = new CopyOnWriteArrayList<>();

        for (int i = 0; i < this.poolSize; i++) {
            final NioEventExecutor eventExecutor = this.executorProvider.get();

            log.debug("Built selector {}", eventExecutor.getEventExecutorId());
            this.eventExecutors.add(eventExecutor);
        }
    }

    public void register(AbstractSelectableChannel channel, GroupPurpose key, Object attachment) {
        for (NioEventExecutor eventExecutor : this.eventExecutors) {
            if(channel.isRegistered() && key == GroupPurpose.IO) break;

            if(attachment != null) {
                eventExecutor.register(channel, key, attachment);
            } else {
                eventExecutor.register(channel, key);
            }
        }
    }

    public void register(AbstractSelectableChannel channel, GroupPurpose key) {
        this.register(channel, key, null);
    }
}
