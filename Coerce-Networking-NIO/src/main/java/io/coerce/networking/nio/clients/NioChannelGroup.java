package io.coerce.networking.nio.clients;

import com.google.common.collect.Maps;
import com.google.inject.Singleton;

import java.util.Map;
import java.util.UUID;

@Singleton
public class NioChannelGroup {
    private final Map<UUID, NioChannel> channels;

    public NioChannelGroup() {
        this.channels = Maps.newConcurrentMap();
    }

    public void registerChannel(final NioChannel channel) {
        this.channels.put(channel.getChannelId(), channel);
    }

    public void deregisterChannel(UUID id) {
        final NioChannel nioChannel = this.channels.get(id);

        if (nioChannel == null) {
            return;
        }

        nioChannel.onDispose();
        this.channels.remove(id);
    }

    public NioChannel getChannelById(UUID id) {
        return this.channels.get(id);
    }
}
