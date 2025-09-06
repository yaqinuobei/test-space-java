

package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import code.traveler.test.space.java.sb.grpc.client.pilot.config.PilotGrpcConstant;

@Service("pilotHeaderClientInterceptor")
@Slf4j
public class PilotHeaderClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel channel) {
        return new
                ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(method, callOptions)) {
                    @Override
                    public void start(Listener<RespT> responseListener, Metadata headers) {
                        headers.put( PilotHeader.PILOT_ID_KEY,callOptions.getOption(PilotGrpcConstant.PilotCallOptions.PILOT_ID_KEY));
                        headers.put( PilotHeader.PILOT_INSTANCE_ID_KEY,callOptions.getOption(PilotGrpcConstant.PilotCallOptions.PILOT_INSTANCE_ID_KEY));
                        headers.put( PilotHeader.START_TIME_KEY,callOptions.getOption(PilotGrpcConstant.PilotCallOptions.START_TIME_KEY));
                        log.info("headers={}",headers);
                        super.start(responseListener, headers);
                    }
                };
    }
}
