package code.traveler.test.space.java.sb.grpc.client.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogGrpcInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        log.info("拦截请求："+method.getFullMethodName());
        return next.newCall(method,callOptions);
    }
}
