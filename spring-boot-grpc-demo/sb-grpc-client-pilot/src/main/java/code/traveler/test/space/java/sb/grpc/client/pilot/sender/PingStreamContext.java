

package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import io.grpc.stub.ClientCallStreamObserver;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import code.traveler.test.space.java.sb.grpc.client.pilot.config.PilotGrpcConstant;
import code.traveler.test.space.java.sb.grpc.client.pilot.service.PilotResponseHandleService;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.MessageFormatUtils;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.Reconnector;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.StatusError;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.StatusErrors;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.StreamId;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.StreamUtils;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotPing;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotRequest;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotResponse;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PilotServiceGrpc;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author original
 */
@Slf4j
public class PingStreamContext {
    // for debug
    private final StreamId streamId;
    private StreamObserver<PPilotRequest> requestObserver;
    private PingClientResponseObserver responseObserver;
    private final Reconnector reconnector;

    private final ScheduledExecutorService retransmissionExecutor;

    private final PilotServiceGrpc.PilotServiceStub pilotServiceStub;

    private PilotResponseHandleService pilotService;

    private long lastResponseTime;

    // 默认3分钟超时
    private final long DEFAULT_PING_RESPONSE_TIME_OUT = 3 * 60 * 1000;

    public PingStreamContext(PilotServiceGrpc.PilotServiceStub pilotServiceStub, Reconnector reconnector, ScheduledExecutorService retransmissionExecutor,
                             PilotResponseHandleService pilotService) {
        Objects.requireNonNull(pilotServiceStub, "pilotServiceStub");

        this.streamId = StreamId.newStreamId("PingStream");
        this.pilotServiceStub = pilotServiceStub;
        this.reconnector = Objects.requireNonNull(reconnector, "reconnector");
        this.retransmissionExecutor = Objects.requireNonNull(retransmissionExecutor, "retransmissionExecutor");
        this.pilotService = Objects.requireNonNull(pilotService, "pilotService");

        this.start();
    }

    public void start() {
        this.responseObserver = new PingClientResponseObserver();
        this.requestObserver = pilotServiceStub.handlePilot(responseObserver);
        PilotStreamObserverRegistry.registryRequestStreamObserver(PilotGrpcConstant.PILOT_GRPC_NAME, requestObserver);
        PilotStreamObserverRegistry.registryResponseStreamObserver(PilotGrpcConstant.PILOT_GRPC_NAME, responseObserver);
    }

    public void close() {
        log.info("{} close()", streamId);
        StreamUtils.close(this.requestObserver);
        this.responseObserver.close();
    }

    private PPilotRequest newPing() {
        PPilotRequest.Builder requestBuilder = PPilotRequest.newBuilder();
        PPilotPing.Builder pingBuilder = PPilotPing.newBuilder();
        requestBuilder.setPing(pingBuilder.build());
        return requestBuilder.build();
    }

    private void updateResponseTime() {
        lastResponseTime = System.currentTimeMillis();
    }

    public boolean responseTimeOut() {
        if (System.currentTimeMillis() - lastResponseTime > DEFAULT_PING_RESPONSE_TIME_OUT) {
            return true;
        }
        return false;
    }

    private class PingClientResponseObserver implements ClientResponseObserver<PPilotRequest, PPilotResponse> {
        private volatile ScheduledFuture<?> pingScheduler;
        private boolean status = true;

        @Override
        public void onNext(PPilotResponse pilotResponse) {
            updateResponseTime();
            pilotService.handler(pilotResponse);
            log.info("{} success:{}", streamId, MessageFormatUtils.debugLog(pilotResponse));
        }

        @Override
        public void onError(Throwable t) {
            updateResponseTime();
            final StatusError statusError = StatusErrors.throwable(t);
            if (statusError.isSimpleError()) {
                log.info("Failed to ping stream, streamId={}, cause={}", streamId, statusError.getMessage());
            } else {
                log.info("Failed to ping stream, streamId={}, cause={}", streamId, statusError.getMessage(), statusError.getThrowable());
            }
            cancelPingScheduler();
            if (!status) {
                return;
            }
            PingStreamContext.this.reconnector.reconnect();
        }

        @Override
        public void onCompleted() {
            updateResponseTime();
            log.info("{} completed", streamId);
            cancelPingScheduler();
            if (!status) {
                return;
            }
            PingStreamContext.this.reconnector.reconnect();
        }

        public void close() {
            cancelPingScheduler();
            this.status = false;
        }

        private void cancelPingScheduler() {
            final ScheduledFuture<?> pingScheduler = this.pingScheduler;
            if (pingScheduler != null) {
                pingScheduler.cancel(false);
            } else {
                log.info("pingScheduler is NULL");
            }
        }

        @Override
        public void beforeStart(final ClientCallStreamObserver<PPilotRequest> requestStream) {
            requestStream.setOnReadyHandler(new Runnable() {
                @Override
                public void run() {
                    log.info("{} onReady", streamId);
                    PingStreamContext.this.reconnector.reset();

                    final Runnable pingRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PPilotRequest pPing = newPing();
                            log.info("{} send new ping", streamId);
                            requestStream.onNext(pPing);
                        }
                    };
                    //1分钟ping一次
                    PingClientResponseObserver.this.pingScheduler = schedule(pingRunnable);
                }
            });
        }
    }

    private ScheduledFuture<?> schedule(Runnable command) {
        try {
            return retransmissionExecutor.scheduleAtFixedRate(command, 0, 1, TimeUnit.MINUTES);
        } catch (RejectedExecutionException e) {
            log.info("Ping scheduling failed");
            return null;
        }
    }

    @Override
    public String toString() {
        return "PingStreamContext{" + streamId + '}';
    }
}
