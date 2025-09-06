package code.traveler.test.space.java.sb.grpc.server.server;

import code.traveler.grpc.server.service.GrpcService;
import com.google.common.collect.Lists;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import code.traveler.test.space.java.sb.grpc.lib.student.StudentGrpc;
import code.traveler.test.space.java.sb.grpc.lib.student.StudentRequest;
import code.traveler.test.space.java.sb.grpc.lib.student.StudentResponse;
import java.util.List;

@GrpcService
@Slf4j
public class StudentServer extends StudentGrpc.StudentImplBase {

    public StreamObserver<StudentRequest> getDetail(StreamObserver<StudentResponse> responseObserver) {

        return new StreamObserver<StudentRequest>() {

            List<StudentRequest> srs = Lists.newArrayList();

            @Override
            public void onNext(StudentRequest studentRequest) {
                log.info("receive request student：{}", studentRequest.toString());
                srs.add(studentRequest);

                StudentResponse sr = StudentResponse
                        .newBuilder()
                        .setName(studentRequest.getName())
                        .setSNum(studentRequest.getSNum())
                        .setAge(11)
                        .setSex(1)
                        .build();
                responseObserver.onNext(sr);

            }

            @Override
            public void onError(Throwable throwable) {
                log.info("receive request error：{}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("receive all request studnet:");
                srs.forEach(studentRequest -> {
                    StudentResponse sr = StudentResponse
                            .newBuilder()
                            .setName(studentRequest.getName())
                            .setSNum(studentRequest.getSNum())
                            .setAge(11)
                            .setSex(1)
                            .build();
                    responseObserver.onNext(sr);
                    log.info(studentRequest.toString());
                });
                responseObserver.onCompleted();
                log.info("receive request complete !!!");
            }
        };
    }
}
