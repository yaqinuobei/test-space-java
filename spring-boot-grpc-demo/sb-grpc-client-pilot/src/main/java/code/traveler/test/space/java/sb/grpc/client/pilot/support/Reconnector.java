

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

/**
 * @author original
 */
public interface Reconnector {

    void reset();

    void reconnect();
}
