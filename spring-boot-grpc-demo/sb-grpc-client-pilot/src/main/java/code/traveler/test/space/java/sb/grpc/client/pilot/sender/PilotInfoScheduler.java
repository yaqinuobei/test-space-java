package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import code.traveler.test.space.java.sb.grpc.client.pilot.config.PilotGrpcConstant;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.PilotInfo;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.PilotInfoFactory;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotRequest;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author panda
 * @description 定时发送pilotInfo
 * @time 2023/7/27 14:44
 */
@Slf4j
public class PilotInfoScheduler {

    // refresh daily
    private static final long DEFAULT_AGENT_INFO_REFRESH_INTERVAL_MS = 24 * 60 * 60 * 1000L;
    // retry every 3 seconds
    private static final long DEFAULT_AGENT_INFO_SEND_INTERVAL_MS = 3 * 1000L;
    // retry 3 times per attempt
    private static final int DEFAULT_MAX_TRY_COUNT_PER_ATTEMPT = 3;
    private long refreshIntervalMs = DEFAULT_AGENT_INFO_REFRESH_INTERVAL_MS;
    private long sendIntervalMs = DEFAULT_AGENT_INFO_SEND_INTERVAL_MS;
    private int maxTryPerAttempt = DEFAULT_MAX_TRY_COUNT_PER_ATTEMPT;

    private PilotInfoFactory pilotInfoFactory;

    private PilotGrpcDataSender pilotGrpcDataSender;

    public PilotInfoScheduler(PilotGrpcDataSender pilotGrpcDataSender) {
        this.pilotGrpcDataSender = pilotGrpcDataSender;
    }

    private class Scheduler {

        private static final long IMMEDIATE = 0L;
        private final Timer timer = new Timer("PilotPingScheduleSender-Timer", true);
        private final Object lock = new Object();
        // protected by lock's monitor
        private boolean isRunning = true;

        private Scheduler() {
            // preload
            PilotInfoSendTask task = new PilotInfoSendTask(SuccessListener.NO_OP);
            task.run();
        }

        public void start() {
            final SuccessListener successListener = new SuccessListener() {
                @Override
                public void onSuccess() {
                    schedule(this, maxTryPerAttempt, refreshIntervalMs, sendIntervalMs);
                }
            };
            log.debug("Start scheduler of pilotInfoSender");
            schedule(successListener, Integer.MAX_VALUE, IMMEDIATE, sendIntervalMs);
        }

        public void refresh() {
            log.debug("Refresh scheduler of pilotInfoSender");
            schedule(SuccessListener.NO_OP, maxTryPerAttempt, IMMEDIATE, sendIntervalMs);
        }

        private void schedule(SuccessListener successListener, int retryCount, long delay, long period) {
            synchronized (lock) {
                if (isRunning) {
                    PilotInfoSendTask task = new PilotInfoSendTask(successListener, retryCount);
                    timer.scheduleAtFixedRate(task, delay, period);
                }
            }
        }

        public void stop() {
            synchronized (lock) {
                isRunning = false;
                timer.cancel();
            }
        }
    }

    private class PilotInfoSendTask extends TimerTask {

        private final SuccessListener taskHandler;
        private final int retryCount;
        private final AtomicInteger counter;

        private PilotInfoSendTask(SuccessListener taskHandler) {
            this(taskHandler, 0);
        }

        private PilotInfoSendTask(SuccessListener taskHandler, int retryCount) {
            this.taskHandler = Objects.requireNonNull(taskHandler, "taskHandler");
            this.retryCount = retryCount;
            this.counter = new AtomicInteger(0);
        }

        @Override
        public void run() {
            int runCount = counter.incrementAndGet();
            if (runCount > retryCount) {
                this.cancel();
                return;
            }
            boolean isSuccessful = sendPilotInfo();
            if (isSuccessful) {
                log.info("PilotInfo sent.");
                this.cancel();
                taskHandler.onSuccess();
            }
        }

        private boolean sendPilotInfo() {
            try {
                StreamObserver<PPilotRequest> requestStreamObserver = PilotStreamObserverRegistry.getRequestStreamObserver(PilotGrpcConstant.PILOT_GRPC_NAME);
                PilotInfo pilotInfo = pilotInfoFactory.createPilotInfo();
                pilotGrpcDataSender.send(pilotInfo);
            } catch (Exception e) {

            }
            return true;
        }
    }

    private interface SuccessListener {
        void onSuccess();

        SuccessListener NO_OP = new SuccessListener() {
            @Override
            public void onSuccess() {
                // noop
            }
        };
    }

}
