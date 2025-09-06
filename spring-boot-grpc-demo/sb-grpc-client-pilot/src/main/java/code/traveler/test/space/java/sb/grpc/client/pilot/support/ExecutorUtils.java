

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author original
 */
public final class ExecutorUtils {

    public static final long DEFAULT_SHUTDOWN_TIMEOUT = 3000;

    private static final Logger logger = LoggerFactory.getLogger(ExecutorUtils.class.getName());

    private ExecutorUtils() {
    }

    public static boolean shutdownExecutorService(String name, ExecutorService executorService) {
        return shutdownExecutorService(name, executorService, DEFAULT_SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public static boolean shutdownExecutorService(String name, ExecutorService executorService, long timeout, TimeUnit unit) {
        if (executorService == null) {
            return false;
        }
        logger.debug("shutdown {}", name);
        executorService.shutdown();
        try {
            final boolean success = executorService.awaitTermination(timeout, unit);
            if (!success) {
                logger.warn("shutdown timeout {}", name);
            }
            return success;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
