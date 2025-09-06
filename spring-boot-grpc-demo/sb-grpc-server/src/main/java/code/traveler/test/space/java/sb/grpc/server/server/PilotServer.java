package code.traveler.test.space.java.sb.grpc.server.server;

import code.traveler.grpc.server.service.GrpcService;
import com.google.common.collect.Lists;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotConfigMessage;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotRequest;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotResponse;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PilotServiceGrpc;
import java.util.List;

@GrpcService
@Slf4j
public class PilotServer extends PilotServiceGrpc.PilotServiceImplBase {

    public io.grpc.stub.StreamObserver<PPilotRequest> handlePilot(io.grpc.stub.StreamObserver<PPilotResponse> responseObserver) {

        return new StreamObserver<PPilotRequest>() {

            List<PPilotRequest> srs = Lists.newArrayList();

            @Override
            public void onNext(PPilotRequest pilotRequest) {
                log.info("receive request pilot：{}", pilotRequest.toString());
                srs.add(pilotRequest);
                PPilotConfigMessage pilotConfigMessage = PPilotConfigMessage
                        .newBuilder()
                        .setAgentImageTag("1")
                        .setAgentImageUrl("v1")
                        .build();

                PPilotResponse sr = PPilotResponse
                        .newBuilder()
                        .setPilotConfig(pilotConfigMessage)
                        .build();
                responseObserver.onNext(sr);

            }

            @Override
            public void onError(Throwable throwable) {
                log.info("receive request error：{}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("receive all request pilot:");
                srs.forEach(pilotRequest -> {
                    PPilotConfigMessage pilotConfigMessage = PPilotConfigMessage
                            .newBuilder()
                            .setAgentImageTag("1")
                            .setAgentImageUrl("v1")
                            .build();
                    PPilotResponse sr = PPilotResponse
                            .newBuilder()
                            .setPilotConfig(pilotConfigMessage)
                            .build();
                    responseObserver.onNext(sr);
                    log.info(pilotRequest.toString());
                });
                responseObserver.onCompleted();
                log.info("receive request complete !!!");
            }
        };
    }
}
