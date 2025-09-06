

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import io.grpc.internal.ExponentialBackoffPolicy;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author original
 */
public class ExponentialBackoffReconnectJob implements ReconnectJob {

    private final long maxBackOffNanos;

    private volatile ExponentialBackoffPolicy exponentialBackoffPolicy = new ExponentialBackoffPolicy();
    private final Runnable runnable;

    public ExponentialBackoffReconnectJob(Runnable runnable) {
        this(runnable, TimeUnit.SECONDS.toNanos(30));
    }

    public ExponentialBackoffReconnectJob(Runnable runnable, long maxBackOffNanos) {
        this.runnable = Objects.requireNonNull(runnable, "runnable");

        Assert.isTrue(maxBackOffNanos > 0, "maxBackOffNanos > 0");
        this.maxBackOffNanos = getMaxBackOffNanos(maxBackOffNanos);
    }

    private long getMaxBackOffNanos(long maxBackOffNanos) {
        if (TimeUnit.SECONDS.toNanos(3) > maxBackOffNanos) {
            return TimeUnit.SECONDS.toNanos(3);
        } else {
            return maxBackOffNanos;
        }
    }

    @Override
    public final void resetBackoffNanos() {
        exponentialBackoffPolicy = new ExponentialBackoffPolicy();
    }

    @Override
    public long nextBackoffNanos() {
        return Math.min(exponentialBackoffPolicy.nextBackoffNanos(), maxBackOffNanos);
    }

    public void run() {
        this.runnable.run();
    }

    @Override
    public String toString() {
        return "ExponentialBackoffReconnectJob{" +
                "maxBackOffNanos=" + maxBackOffNanos +
                ", exponentialBackoffPolicy=" + exponentialBackoffPolicy +
                ", runnable=" + runnable +
                '}';
    }
}
