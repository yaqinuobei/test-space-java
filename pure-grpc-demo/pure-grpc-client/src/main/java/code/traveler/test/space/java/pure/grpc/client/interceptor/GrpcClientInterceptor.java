package code.traveler.test.space.java.pure.grpc.client.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Context;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.CALLOPTIONS_KEY_SNUM;
import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.CONTEXT_KEY_SNUM;
import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.METADATE_KEY_SNUM;
import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@Service("grpcClientInterceptor")
@Slf4j
public class GrpcClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        return new
                ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
                    @Override
                    public void start(Listener<RespT> responseListener, Metadata headers) {
                        log.info("Added metadata");
                        headers.put(Metadata.Key.of("HOSTNAME", ASCII_STRING_MARSHALLER), "MY_HOST");
                        Context.current().withValue(CONTEXT_KEY_SNUM, callOptions.getOption(CALLOPTIONS_KEY_SNUM));
                        headers.put(METADATE_KEY_SNUM,callOptions.getOption(CALLOPTIONS_KEY_SNUM));
                        super.start(responseListener, headers);
                    }

                    @Override
                    public void sendMessage(ReqT message) {
                        log.info("拦截发送信息：{}",message);
                        delegate().sendMessage(message);
                    }
                };
    }
}
