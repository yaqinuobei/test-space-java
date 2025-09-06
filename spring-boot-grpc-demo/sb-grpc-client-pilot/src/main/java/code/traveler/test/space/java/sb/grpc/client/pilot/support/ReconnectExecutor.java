

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * @author original
 */
public class ReconnectExecutor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile boolean shutdown;
    private final ScheduledExecutorService scheduledExecutorService;
    private final AtomicLong rejectedCounter = new AtomicLong();

    public ReconnectExecutor(ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = Objects.requireNonNull(scheduledExecutorService, "scheduledExecutorService");
    }

    private void execute0(Runnable command) {
        Objects.requireNonNull(command, "command");

        if (shutdown) {
            logger.debug("already shutdown");
            return;
        }
        if (command instanceof ReconnectJob) {
            ReconnectJob reconnectJob = (ReconnectJob) command;
            try {
                scheduledExecutorService.schedule(reconnectJob, reconnectJob.nextBackoffNanos(), TimeUnit.NANOSECONDS);
            } catch (RejectedExecutionException e) {
                final long failCount = rejectedCounter.incrementAndGet();
                logger.info("{} reconnectJob scheduled fail {}", command, failCount);
            }
        } else {
            throw new IllegalArgumentException("unknown command type " + command);
        }
    }

    public void start() {
        shutdown = false;
    }

    public void close() {
        shutdown = true;
    }

    public Reconnector newReconnector(Runnable reconnectJob) {
        Objects.requireNonNull(reconnectJob, "reconnectJob");
        if (logger.isInfoEnabled()) {
            logger.info("newReconnector({})", reconnectJob);
        }

        final Executor dispatch = new Executor() {
            @Override
            public void execute(Runnable command) {
                ReconnectExecutor.this.execute0(command);
            }
        };
        final ReconnectJob reconnectJobWrap = wrapReconnectJob(reconnectJob);
        return new ReconnectAdaptor(dispatch, reconnectJobWrap);
    }


    private ReconnectJob wrapReconnectJob(Runnable runnable) {
        return new ExponentialBackoffReconnectJob(runnable);
    }
}
