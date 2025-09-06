

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author original
 */
public final class StreamId {
    private static final AtomicLong idAllocator = new AtomicLong();

    private final String name;

    public static StreamId newStreamId(String name) {
        return new StreamId(name, idAllocator.incrementAndGet());
    }

    private StreamId(String name, long id) {
        Objects.requireNonNull(name, "name");
        this.name = name + "-" + id;
    }

    @Override
    public String toString() {
        return name;
    }
}
