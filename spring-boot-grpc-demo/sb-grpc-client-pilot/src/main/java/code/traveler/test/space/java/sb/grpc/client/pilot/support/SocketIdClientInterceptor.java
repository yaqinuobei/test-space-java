

package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

import code.traveler.test.space.java.sb.grpc.client.pilot.message.PilotHeader;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author original
 */
public class SocketIdClientInterceptor implements ClientInterceptor {

    private final AtomicLong socketIdAllocator = new AtomicLong();

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        final ClientCall<ReqT, RespT> clientCall = next.newCall(method, callOptions);
        final ClientCall<ReqT, RespT> forwardClientCall = new SimpleForwardingClientCall<ReqT, RespT>(clientCall) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                final String socketId = nextSocketId();
                headers.put(PilotHeader.SOCKET_ID, socketId);
                super.start(responseListener, headers);
            }
        };
        return forwardClientCall;
    }

    private String nextSocketId() {
        return String.valueOf(socketIdAllocator.incrementAndGet());
    }

}
