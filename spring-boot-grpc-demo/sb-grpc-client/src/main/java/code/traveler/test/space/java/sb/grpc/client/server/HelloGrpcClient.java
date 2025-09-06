package code.traveler.test.space.java.sb.grpc.client.server;

import code.traveler.grpc.client.inject.GrpcClient;
import com.google.common.collect.Lists;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import code.traveler.test.space.java.sb.grpc.lib.HelloReply;
import code.traveler.test.space.java.sb.grpc.lib.HelloRequest;
import code.traveler.test.space.java.sb.grpc.lib.SimpleGrpc;
import java.util.List;

@Service
@Slf4j
public class HelloGrpcClient {

    @GrpcClient("spring-boot-grpc-service")
    private SimpleGrpc.SimpleBlockingStub simpleBlockingStub;

    @GrpcClient("spring-boot-grpc-service")
    private SimpleGrpc.SimpleStub simpleStub;

    @GrpcClient("spring-boot-grpc-service")
    private SimpleGrpc.SimpleFutureStub simpleFutureStub;

    //阻塞返回结果
    public String sendMessageBlocking(String name) {
        try {
            log.info("发送：{}", name);
            HelloReply response = this.simpleBlockingStub.sayHello(HelloRequest
                    .newBuilder()
                    .setName(name)
                    .build());
            log.info("发送成功，返回信息为：{}", response.getMessage());

            return response.getMessage();
        } catch (StatusRuntimeException e) {
            log.info("发送消息失败，异常信息:{},异常详情:{}", e.getMessage(), e);
            return "FAILED with " + e
                    .getStatus()
                    .getCode()
                    .name();
        }
    }

    //异步返回结果
    public void sendMessage(String name) {
        try {

            List<HelloReply> responses = Lists.newArrayList();
            StreamObserver<HelloReply> responseObserver = new StreamObserver<HelloReply>() {
                @Override
                public void onNext(HelloReply helloReply) {
                    log.info("receive response helloReply:{}", helloReply.toString());
                    responses.add(helloReply);
                }

                @Override
                public void onError(Throwable throwable) {
                    log.info("receive response error:{}", throwable.getMessage());
                }

                @Override
                public void onCompleted() {
                    log.info("response all helloReply:");
                    responses.forEach(resp -> {
                        log.info(resp.toString());
                    });
                    log.info("response complete !!!");
                }
            };
            HelloRequest request = HelloRequest
                    .newBuilder()
                    .setName(name)
                    .build();
            log.info("send request：{}", request.toString());
            this.simpleStub.sayHello(request, responseObserver);
        } catch (StatusRuntimeException e) {
            log.info("发送消息失败，异常信息:{},异常详情:{}", e.getMessage(), e);
        }
    }


}
