package io.coerce.networking.nio.clients;

import io.coerce.networking.nio.clients.io.NioChannelReader;
import io.coerce.networking.nio.clients.io.NioChannelWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

public class NioChannel {
    private final UUID channelId;
    private final SocketChannel socketChannel;

    private final NioChannelWriter writer;
    private final NioChannelReader reader;

    private final Logger log = LogManager.getLogger(NioChannel.class);

    public NioChannel(final SocketChannel socketChannel, final NioChannelWriter writer, final NioChannelReader reader) {
        this.channelId = UUID.randomUUID();
        this.socketChannel = socketChannel;

        this.writer = writer;
        this.reader = reader;
    }

    public void onConnection() {
        log.debug("Connection initialised");
    }

    public void onReceiveData() {

        final String httpResponse = "Content-Length:9\n" +
                "Content-Type:text/html\n" +
                "Date:Sat, 30 Apr 2016 13:38:59 GMT\n" +
                "ETag:\"56d48ea2-a\"\n" +
                "Last-Modified:Mon, 29 Feb 2016 18:32:02 GMT\n" +
                "Server:nginx/1.4.6 (Ubuntu)\n\n" +
                "OpenHabbo";

        log.info("Writing data {}", httpResponse);

        this.writer.writeAndThen(httpResponse.getBytes(), (socketChannel) -> {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void onDispose() {
        log.debug("Connection disposed");
    }

    public UUID getChannelId() {
        return this.channelId;
    }

    public SocketAddress getAddress() {
        try {
            return this.socketChannel.getRemoteAddress();
        } catch (Exception e) {
            return null;
        }
    }

    public SocketChannel getSocketChannel() {
        return this.socketChannel;
    }

    public NioChannelWriter getWriter() {
        return this.writer;
    }

    public NioChannelReader getReader() {
        return this.reader;
    }
}
