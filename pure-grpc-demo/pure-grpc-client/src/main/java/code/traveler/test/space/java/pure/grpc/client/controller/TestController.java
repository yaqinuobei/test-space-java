package code.traveler.test.space.java.pure.grpc.client.controller;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import code.traveler.test.space.java.pure.grpc.client.server.GrpcClient;
import code.traveler.test.space.java.pure.grpc.lib.cat.CatRequest;

@RestController
@RequestMapping("/v1/test")
@Slf4j
public class TestController {

    @Autowired
    private GrpcClient grpcClient;

    @GetMapping("/getDetial")
    public String getDetail(String sNum) {
        StreamObserver<CatRequest> catRequestStreamObserver = grpcClient.getClient(sNum);
        CatRequest request = CatRequest
                .newBuilder()
                .setSNum(sNum)
                .build();
        catRequestStreamObserver.onNext(request);
        return "success";
    }

    @GetMapping("/finish")
    public String finish(String sNum) {
        grpcClient.finishClient(sNum);
        return "success";
    }

}
