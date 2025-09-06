package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import code.traveler.test.space.java.sb.grpc.client.pilot.support.ClientReconnectEventListener;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.THandler;

/**
 * @author emeroad
 */
public interface EnhancedDataSender<ReqT, ResT> extends DataSender<ReqT> {

    default boolean request(ReqT data) {
        throw new UnsupportedOperationException("unsupported operation request(data)");
    }

    ;

    default boolean request(ReqT data, int retry) {
        throw new UnsupportedOperationException("unsupported operation request(data)");
    }

    ;

    default boolean request(ReqT data, FutureListener<ResT> listener) {
        throw new UnsupportedOperationException("unsupported operation request(data)");
    }

    ;

    boolean addReconnectEventListener(ClientReconnectEventListener eventListener);

    boolean removeReconnectEventListener(ClientReconnectEventListener eventListener);

    void addReconnectJobHandler(THandler handler);

    Runnable getReconnectJob();

}
