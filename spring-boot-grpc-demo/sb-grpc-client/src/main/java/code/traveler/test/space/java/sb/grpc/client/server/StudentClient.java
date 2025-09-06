package code.traveler.test.space.java.sb.grpc.client.server;

import code.traveler.grpc.client.inject.GrpcClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import code.traveler.test.space.java.sb.grpc.lib.student.StudentGrpc;
import code.traveler.test.space.java.sb.grpc.lib.student.StudentRequest;
import code.traveler.test.space.java.sb.grpc.lib.student.StudentResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class StudentClient {

    @GrpcClient("spring-boot-grpc-student-service")
    private StudentGrpc.StudentBlockingStub studentBlockingStub;

    @GrpcClient("spring-boot-grpc-student-service")
    private StudentGrpc.StudentFutureStub studentFutureStub;

    @GrpcClient("spring-boot-grpc-student-service")
    private StudentGrpc.StudentStub studentStub;

    //存放用户请求的连接
    private Map<Integer, StreamObserver<StudentRequest>> studentRequestObserverMap = Maps.newConcurrentMap();

    public void getStudentDetai(Integer sNum, String name) {
        try {
            StreamObserver<StudentRequest> requestStreamObserver = studentRequestObserverMap.get(sNum);//复用已有连接
            if (Objects.isNull(requestStreamObserver)) {
                this.buildStudentRequestObserver(sNum);
                requestStreamObserver = studentRequestObserverMap.get(sNum);
            }
            StudentRequest request = StudentRequest
                    .newBuilder()
                    .setName(name)
                    .setSNum(sNum)
                    .build();
            log.info("sent request student：{}", request.toString());
            requestStreamObserver.onNext(request);
        } catch (StatusRuntimeException e) {
            log.info("发送消息失败，异常信息:{},异常详情:{}", e.getMessage(), e);
        }
    }

    //构建双向流请求观察对象
    private synchronized void buildStudentRequestObserver(Integer sNum) {
        if (Objects.nonNull(studentRequestObserverMap.get(sNum))) {
            return;
        }
        List<StudentResponse> responses = Lists.newArrayList();
        StreamObserver<StudentResponse> responseObserver = new StreamObserver<StudentResponse>() {
            @Override
            public void onNext(StudentResponse studentResponse) {
                responses.add(studentResponse);
                log.info("recerve server student:{}", studentResponse.toString());
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("recerve server error:{}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("reveive all response student:");
                for (StudentResponse resp : responses) {
                    log.info(resp.toString());
                }
                log.info("reveive response complete !!!");
            }
        };
        StreamObserver<StudentRequest> requestStreamObserver = this.studentStub.getDetail(responseObserver);
        studentRequestObserverMap.put(sNum, requestStreamObserver);
    }
}
