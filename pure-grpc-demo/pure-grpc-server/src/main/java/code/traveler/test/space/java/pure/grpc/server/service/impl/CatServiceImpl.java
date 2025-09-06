package code.traveler.test.space.java.pure.grpc.server.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import code.traveler.test.space.java.pure.grpc.lib.cat.CatRequest;
import code.traveler.test.space.java.pure.grpc.lib.cat.CatResponse;
import code.traveler.test.space.java.pure.grpc.lib.service.CatServiceGrpc;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.CONTEXT_KEY_SNUM;

@Slf4j
@Service("catServiceImpl")
public class CatServiceImpl extends CatServiceGrpc.CatServiceImplBase {

    private Map<String, StreamObserver<CatRequest>> requestObserverMap = Maps.newConcurrentMap();

    private Map<String, List<CatRequest>> requestsMap = Maps.newConcurrentMap();

    private Map<String, String> numNameMap = new HashMap() {{
        put("1", "hong");
        put("2", "hei");
        put("3", "huang");
        put("4", "lan");
    }};

    @Override
    public StreamObserver<CatRequest> getDetail(StreamObserver<CatResponse> responseObserver) {

        StreamObserver<CatRequest> catRequestStreamObserver = new StreamObserver<CatRequest>() {

            List<CatRequest> requestList = new LinkedList<>();

            public Queue<CatRequest> requestQueueOuter = new LinkedBlockingDeque<>();

            @Override
            public void onNext(CatRequest catRequest) {
                log.info("receive request:{}", catRequest.toString());
                requestList.add(catRequest);
                log.info("do business");
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("receive error:{}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("集中处理所有请求：");
                requestList.forEach(request -> {
                    responseObserver.onNext(CatResponse
                            .newBuilder()
                            .setSNum(request.getSNum())
                            .setName(numNameMap.get(request.getSNum()))
                            .build());
                });
                List<CatRequest> requestsOuter = requestsMap.get(CONTEXT_KEY_SNUM.get());
                requestsMap.remove(CONTEXT_KEY_SNUM.get());
                if (!CollectionUtils.isEmpty(requestsOuter)) {
                    requestsOuter.forEach(request -> {
                        responseObserver.onNext(CatResponse
                                .newBuilder()
                                .setSNum(request.getSNum())
                                .setName(numNameMap.get(request.getSNum()))
                                .build());
                    });
                }
                responseObserver.onCompleted();
                log.info("request finish!!!");
            }
        };

        log.info("this is getDetail");

        requestObserverMap.put(CONTEXT_KEY_SNUM.get(), catRequestStreamObserver);

        return catRequestStreamObserver;
    }

    //接收外部的请求
    public void addRequest(String sNum) {
        List<CatRequest> requests = requestsMap.get(sNum);
        if (CollectionUtils.isEmpty(requests)) {
            requests = Lists.newArrayList();
            requestsMap.put(sNum, requests);
        }
        requests.add(CatRequest
                .newBuilder()
                .setSNum(sNum)
                .build());
    }
}
