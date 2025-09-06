package code.traveler.test.space.java.pure.grpc.client.server;

import com.google.common.collect.Maps;
import io.grpc.ClientInterceptor;
import io.grpc.Context;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import code.traveler.test.space.java.pure.grpc.lib.cat.CatRequest;
import code.traveler.test.space.java.pure.grpc.lib.cat.CatResponse;
import code.traveler.test.space.java.pure.grpc.lib.service.CatServiceGrpc;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.CALLOPTIONS_KEY_SNUM;
import static code.traveler.test.space.java.pure.grpc.lib.constant.GrpcConstant.CONTEXT_KEY_SNUM;


@Service
@Slf4j
public class GrpcClient {

    @Autowired
    @Qualifier("grpcClientInterceptor")
    private ClientInterceptor grpcClientInterceptor;

    //存放用户请求的连接
    private Map<String,StreamObserver<CatRequest>> studentRequestObserverMap = Maps.newConcurrentMap();

    public StreamObserver<CatRequest> getClient(String sNum){

        if(Objects.isNull(studentRequestObserverMap.get(sNum))){
            this.buildStudentRequestObserver(sNum);
        }
        return studentRequestObserverMap.get(sNum);
    }

    public void finishClient(String sNum){
        if(Objects.nonNull(studentRequestObserverMap.get(sNum))){
            studentRequestObserverMap.get(sNum).onCompleted();
            studentRequestObserverMap.remove(sNum);
        }
    }

    //构建双向流请求观察对象
    private synchronized void buildStudentRequestObserver(String sNum){
        if(Objects.nonNull(studentRequestObserverMap.get(sNum))){
            return;
        }
        log.info("开始构建连接，sNum:{}",sNum);
        ManagedChannel managedChannel =
                ManagedChannelBuilder.forTarget("localhost:9090").usePlaintext().intercept(grpcClientInterceptor).keepAliveWithoutCalls(true).executor(
                        Executors.newFixedThreadPool(16)).build();

        CatServiceGrpc.CatServiceStub catServiceStub = CatServiceGrpc.newStub(managedChannel);


        StreamObserver<CatResponse> responseStreamObserver = new StreamObserver<CatResponse>(){

            private String sNumInner = sNum;
            @Override
            public void onNext(CatResponse catResponse) {
                log.info("receive response catResponse:{}",catResponse.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("receive response error:{}",throwable.getMessage());
                studentRequestObserverMap.remove(sNumInner);
            }

            @Override
            public void onCompleted() {
                log.info("receive response finish !!!");
                studentRequestObserverMap.remove(sNumInner);
            }
        };
        Context.current().withValue(CONTEXT_KEY_SNUM, sNum.toString());
        StreamObserver<CatRequest> catRequestStreamObserver =
                catServiceStub.withOption(CALLOPTIONS_KEY_SNUM,sNum.toString()).getDetail(responseStreamObserver);
        studentRequestObserverMap.put(sNum.toString(),catRequestStreamObserver);
    }
}
