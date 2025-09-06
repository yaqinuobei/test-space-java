package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import io.grpc.Metadata;

/**
 * @author original
 */
public interface HeaderReader<H> {
    H extract(Metadata headers);
}
