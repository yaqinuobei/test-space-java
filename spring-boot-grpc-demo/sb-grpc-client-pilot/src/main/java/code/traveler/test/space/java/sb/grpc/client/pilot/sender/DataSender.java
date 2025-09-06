

package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

/**
 * @author emeroad
 * @author netspider
 */
public interface DataSender<T> {
    void start();

    boolean send(T data);

    void stop();

    void shutdown();
}
