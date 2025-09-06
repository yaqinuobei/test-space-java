package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import com.google.protobuf.GeneratedMessageV3;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import code.traveler.test.space.java.sb.grpc.client.pilot.config.PilotGrpcConstant;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.MessageConverter;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.MetaDataType;
import code.traveler.test.space.java.sb.grpc.client.pilot.service.PilotResponseHandleService;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.ClientReconnectEventListener;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.CloseListener;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.ReconnectExecutor;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.Reconnector;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.THandler;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotRequest;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PilotServiceGrpc;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author jaehong.kim
 */
@Slf4j
public class PilotGrpcDataSender<ReqT, ResT> implements EnhancedDataSender<ReqT, ResT>, CloseListener {

    //发送内容转成grpc格式
    private MessageConverter<MetaDataType, GeneratedMessageV3> messageConverter;

    //重连任务执行者，同时负责创建重连器
    private ReconnectExecutor reconnectExecutor;
    //重连器
    private Reconnector reconnector;
    //重连job
    private final Runnable reconnectJob;
    //重连执行
    private final List<THandler> reconnectHandlers = new ArrayList<>();

    private volatile PingStreamContext pingStreamContext;

    private boolean shutdown = true;
    private List<ClientReconnectEventListener> reconnectEventListeners = new ArrayList<>();

    public PilotGrpcDataSender(final PilotServiceGrpc.PilotServiceStub pilotServiceStub, final PilotResponseHandleService pilotResponseHandleService,
                               final MessageConverter<MetaDataType, GeneratedMessageV3> messageConverter, final ReconnectExecutor reconnectExecutor,
                               final ScheduledExecutorService retransmissionExecutor) {
        this.reconnectExecutor = reconnectExecutor;
        this.messageConverter = messageConverter;
        this.reconnectJob = new Runnable() {
            @Override
            public void run() {
                log.info("execute agent reconnect job");
                pingStreamContext = new PingStreamContext(pilotServiceStub, reconnector, retransmissionExecutor, pilotResponseHandleService);
                for (THandler handler : reconnectHandlers) {
                    handler.handle();
                }
            }
        };
    }

    @Override
    public void start() {
        if (!shutdown) {
            return;
        }
        this.shutdown = false;
        reconnectExecutor.start();
        this.reconnector = reconnectExecutor.newReconnector(reconnectJob);
        this.reconnectJob.run();
    }

    //    @Override
    public void keepConnection() {
        if (!this.pingStreamContext.responseTimeOut()) {
            return;
        }
        log.info("Ping stream last response is timeout!");
        pingStreamContext.close();
        this.reconnectJob.run();
    }

    @Override
    public boolean send(ReqT data) {
        if (data instanceof MetaDataType) {
            GeneratedMessageV3 message = messageConverter.toMessage((MetaDataType) data);
            if (message instanceof PPilotRequest) {
                StreamObserver<PPilotRequest> requestStreamObserver = PilotStreamObserverRegistry.getRequestStreamObserver(PilotGrpcConstant.PILOT_GRPC_NAME);
                requestStreamObserver.onNext((PPilotRequest) message);
                return true;
            }
        }
        return false;
    }

    @Override
    public void stop() {
        if (shutdown) {
            return;
        }
        this.shutdown = true;
        log.info("Stop {}", "pilotGrpcDataSender");
        final ReconnectExecutor reconnectExecutor = this.reconnectExecutor;
        if (reconnectExecutor != null) {
            reconnectExecutor.close();
        }
        final PingStreamContext pingStreamContext = this.pingStreamContext;
        if (pingStreamContext != null) {
            pingStreamContext.close();
        }
    }

    @Override
    public void shutdown() {
        log.info("Shutdown {}", "pilotGrpcDataSender");
        this.stop();
//        this.release();
    }

    @Override
    public void addReconnectJobHandler(THandler handler) {
        this.reconnectHandlers.add(handler);
    }

    @Override
    public Runnable getReconnectJob() {
        return this.reconnectJob;
    }

    @Override
    public boolean addReconnectEventListener(ClientReconnectEventListener eventListener) {
        this.reconnectEventListeners.add(eventListener);
        return true;
    }

    @Override
    public boolean removeReconnectEventListener(ClientReconnectEventListener eventListener) {
        this.reconnectEventListeners.remove(eventListener);
        return true;
    }

    @Override
    public void close() {
        this.shutdown();
    }

    @Override
    public Integer getCloseOrder() {
        return PilotGrpcConstant.CloseOrder.PILOT_GRPC_DATA_SENDER;
    }
}