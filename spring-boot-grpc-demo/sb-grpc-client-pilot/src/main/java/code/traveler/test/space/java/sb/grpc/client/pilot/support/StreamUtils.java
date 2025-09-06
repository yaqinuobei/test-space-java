

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author original
 */
public final class StreamUtils {
    private static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    private StreamUtils() {
    }

    public static void close(final StreamObserver<?> streamObserver) {
        if (streamObserver != null) {
            try {
                streamObserver.onCompleted();
            } catch (Exception e) {
                logger.error("stream[{}] complete error: {}", streamObserver, e.getMessage());
            }
        }
    }
}
