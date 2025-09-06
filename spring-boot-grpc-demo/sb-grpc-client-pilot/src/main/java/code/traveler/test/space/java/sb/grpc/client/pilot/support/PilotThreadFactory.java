

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author emeroad
 */
public class PilotThreadFactory implements ThreadFactory {
    public static final String DEFAULT_THREAD_NAME_PREFIX = "Pilot-";
    private final static AtomicInteger FACTORY_NUMBER = new AtomicInteger(0);
    private final AtomicInteger threadNumber = new AtomicInteger(0);

    private final String threadPrefix;
    private final boolean daemon;


    public PilotThreadFactory() {
        this("Pilot", false);
    }

    public PilotThreadFactory(String threadName) {
        this(threadName, false);
    }

    public PilotThreadFactory(String threadName, boolean daemon) {
        Objects.requireNonNull(threadName, "threadName");

        this.threadPrefix = prefix(threadName, FACTORY_NUMBER.getAndIncrement());
        this.daemon = daemon;
    }

    private String prefix(String threadName, int factoryId) {
        final StringBuilder buffer = new StringBuilder(32);
        buffer.append(threadName);
        buffer.append('(');
        buffer.append(factoryId);
        buffer.append('-');
        return buffer.toString();
    }

    @Override
    public Thread newThread(Runnable job) {
        String newThreadName = createThreadName();
        Thread thread = new Thread(job, newThreadName);
        if (daemon) {
            thread.setDaemon(true);
        }
        return thread;
    }

    private String createThreadName() {
        StringBuilder buffer = new StringBuilder(threadPrefix.length() + 8);
        buffer.append(threadPrefix);
        buffer.append(threadNumber.getAndIncrement());
        buffer.append(')');
        return buffer.toString();
    }

    public static ThreadFactory createThreadFactory(String threadName) {
        return createThreadFactory(threadName, false);
    }

    public static ThreadFactory createThreadFactory(String threadName, boolean daemon) {
        return new PilotThreadFactory(threadName, daemon);
    }
}
