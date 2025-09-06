package code.traveler.test.space.java.pure.grpc.server.interceptor;

import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.CONTEXT_KEY_SNUM;
import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.METADATE_KEY_SNUM;

@Service("grpcServerInterceptor")
@Slf4j
public class GrpcServerInterceptor implements ServerInterceptor{

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String sNum = validateSNum(metadata);
        if (sNum == null) { // this is optional, depending on your needs
            // Assume user not authenticated
            serverCall.close(Status.UNAUTHENTICATED.withDescription("缺失sNum，认证不通过"),
                    new Metadata());
            return new ServerCall.Listener() {};
        }
        Context context = Context.current().withValue(CONTEXT_KEY_SNUM, sNum);
        return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
    }

    //校验必须信息
    private String validateSNum(Metadata metadata){

        return metadata.get(METADATE_KEY_SNUM);
    }
}
