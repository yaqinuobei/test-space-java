

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author original
 */
public class ReconnectAdaptor implements Reconnector {
    private final Executor executor;
    private final ReconnectJob reconnectJob;

    public ReconnectAdaptor(Executor executor, ReconnectJob reconnectJob) {
        this.executor = Objects.requireNonNull(executor, "executor");
        this.reconnectJob = Objects.requireNonNull(reconnectJob, "reconnectJob");
    }


    @Override
    public void reset() {
        reconnectJob.resetBackoffNanos();
    }

    @Override
    public void reconnect() {
        executor.execute(reconnectJob);
    }
}
