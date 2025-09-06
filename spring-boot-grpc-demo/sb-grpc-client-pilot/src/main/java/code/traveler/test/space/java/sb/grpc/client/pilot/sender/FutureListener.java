package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import java.util.concurrent.Future;

public interface FutureListener<T> {
    void onComplete(Future<T> future);
}