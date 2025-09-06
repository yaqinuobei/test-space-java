package code.traveler.test.space.java.sb.grpc.server.server;

import code.traveler.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import code.traveler.test.space.java.sb.grpc.lib.HelloReply;
import code.traveler.test.space.java.sb.grpc.lib.HelloRequest;
import code.traveler.test.space.java.sb.grpc.lib.SimpleGrpc;
import java.util.Date;

@GrpcService
@Slf4j
public class HelloGrpcServer extends SimpleGrpc.SimpleImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply
                .newBuilder()
                .setMessage("你好， " + request.getName() + ", " + new Date())
                .build();
        log.info("接收到请求：{}", reply.getMessage());
        responseObserver.onNext(reply);
//        responseObserver.onCompleted();
    }
}
