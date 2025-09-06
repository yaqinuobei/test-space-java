package code.traveler.test.space.java.sb.grpc.server.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 *@description 拦截打印日志
 *@author panda
 *@time 2023/7/6 17:42
 */
@Slf4j
public class LogGrpcInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        log.info("拦截请求："+serverCall.getMethodDescriptor().getFullMethodName());
        return serverCallHandler.startCall(serverCall,metadata);
    }
}
