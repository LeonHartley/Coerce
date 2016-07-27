package io.coerce.networking.nio.events;

public class NioEventExecutorWatchdog implements Runnable {
    public static NioEventExecutorWatchdog watchdog;

    private final Thread globalWatchdogThread;

    public NioEventExecutorWatchdog() {
        this.globalWatchdogThread = new Thread(this);
    }

    @Override
    public void run() {

    }

    public static NioEventExecutorWatchdog getInstance() {
        if(watchdog == null) {
            watchdog = new NioEventExecutorWatchdog();
        }

        return watchdog;
    }
}
