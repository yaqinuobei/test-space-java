

package code.traveler.test.space.java.sb.grpc.client.pilot.support;


/**
 * @author original
 */
public interface ReconnectJob extends Runnable {

    void resetBackoffNanos();

    long nextBackoffNanos();

}
