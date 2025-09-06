package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import io.grpc.stub.StreamObserver;

import code.traveler.test.space.java.sb.grpc.client.pilot.support.StreamUtils;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotRequest;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotResponse;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author panda
 * @description 注册请求与响应streamObserver
 * @time 2023/7/28 9:30
 */
public class PilotStreamObserverRegistry {

    //请求streamObserver
    private static Map<String, StreamObserver<PPilotRequest>> requestStreamObserverMap = new ConcurrentHashMap<>();

    //响应streamObserver
    private static Map<String, StreamObserver<PPilotResponse>> responseStreamObserverMap = new ConcurrentHashMap<>();

    /**
     * @param name
     * @param requestStreamObserver
     * @return void
     * @description 注册请求streamObserver
     * @author panda
     * @time 2023/7/28 9:32
     */
    public static void registryRequestStreamObserver(String name, StreamObserver<PPilotRequest> requestStreamObserver) {
        requestStreamObserverMap.put(name, requestStreamObserver);
    }

    /**
     * @param name
     * @return void
     * @description 移除请求streamObserver
     * @author panda
     * @time 2023/7/28 9:32
     */
    public static void removeRequestStreamObserver(String name) {
        StreamObserver<PPilotRequest> requestStreamObserver = requestStreamObserverMap.remove(name);
        if (Objects.nonNull(requestStreamObserver)) {
            StreamUtils.close(requestStreamObserver);
        }
    }

    /**
     * @param name
     * @return io.support.stub.StreamObserver<com.navercorp.pinpoint.support.pilot.PPilotRequest>
     * @description 获取请求streamObserver
     * @author panda
     * @time 2023/7/28 9:33
     */
    public static StreamObserver<PPilotRequest> getRequestStreamObserver(String name) {
        return requestStreamObserverMap.get(name);
    }

    /**
     * @param name
     * @param responseStreamObserver
     * @return void
     * @description 注册响应streamObserver
     * @author panda
     * @time 2023/7/28 9:32
     */
    public static void registryResponseStreamObserver(String name, StreamObserver<PPilotResponse> responseStreamObserver) {
        responseStreamObserverMap.put(name, responseStreamObserver);
    }

    /**
     * @param name
     * @return void
     * @description 移除响应streamObserver
     * @author panda
     * @time 2023/7/28 9:32
     */
    public static void removeReponseStreamObserver(String name) {
        StreamObserver<PPilotResponse> responseStreamObserver = responseStreamObserverMap.remove(name);
    }

    /**
     * @param name
     * @return io.support.stub.StreamObserver<com.navercorp.pinpoint.support.pilot.PPilotResponse>
     * @description 获取响应streamObserver
     * @author panda
     * @time 2023/7/28 9:33
     */
    public static StreamObserver<PPilotResponse> getResponseStreamObserver(String name) {
        return responseStreamObserverMap.get(name);
    }
}
