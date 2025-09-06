package code.traveler.test.space.java.sb.grpc.client.pilot.config;

import code.traveler.grpc.client.channelfactory.GrpcChannelConfigurer;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import code.traveler.test.space.java.sb.grpc.client.pilot.support.CloseListener;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.ExecutorUtils;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.PilotThreadFactory;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author panda
 * @description pinpoint默认grpc channel配置加载
 * @time 2023/7/30 11:28
 */
@Component
@Slf4j
public class PinpointGrpcChannelConfigurer implements GrpcChannelConfigurer, CloseListener {

    //根据grpc名称存executor，用于最终关闭
    private Map<String, ExecutorService> executorServiceMap = new ConcurrentHashMap<>();
    //根据grpc名称存eventLoopExecutor，用于最终关闭
    private Map<String, ExecutorService> eventLoopExecutorMap = new ConcurrentHashMap<>();
    //根据grpc名称存eventLoopGroup，用于最终关闭
    private Map<String, EventLoopGroup> eventLoopGroupMap = new ConcurrentHashMap<>();


    /**
     * @param builder
     * @param name
     * @return void
     * @description 填充配置
     * @author panda
     * @time 2023/7/30 11:31
     */
    @Override
    public void accept(ManagedChannelBuilder<?> builder, String name) {
        builder.defaultLoadBalancingPolicy(PilotGrpcConstant.LBPolicyEnum.pick_first.name());
        builder.maxInboundMetadataSize(PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_MAX_HEADER_LIST_SIZE);
        builder.maxInboundMessageSize(PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_MAX_MESSAGE_SIZE);
        ((NettyChannelBuilder) builder).flowControlWindow(PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_FLOW_CONTROL_WINDOW);
        ((NettyChannelBuilder) builder).idleTimeout(PilotGrpcConstant.PinpointDefaultChannel.IDLE_TIMEOUT_MILLIS_DISABLE, TimeUnit.MILLISECONDS);
        // ChannelOption
        ((NettyChannelBuilder) builder).withOption(ChannelOption.TCP_NODELAY, true);
        ((NettyChannelBuilder) builder).withOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_CONNECT_TIMEOUT);

        final WriteBufferWaterMark writeBufferWaterMark = new WriteBufferWaterMark(PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_WRITE_BUFFER_LOW_WATER_MARK,
                PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_WRITE_BUFFER_HIGH_WATER_MARK);
        ((NettyChannelBuilder) builder).withOption(ChannelOption.WRITE_BUFFER_WATER_MARK, writeBufferWaterMark);

        builder.maxTraceEvents(PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_MAX_TRACE_EVENT);
        configureExecutor((NettyChannelBuilder) builder, name);
        ChannelType channelType = getChannelType();
        configureChannelType((NettyChannelBuilder) builder, channelType);
        configureEventLoopGroup((NettyChannelBuilder) builder, channelType, name);
    }

    private void configureExecutor(NettyChannelBuilder builder, String name) {
        ExecutorService executorService = newExecutorService(name + "-Channel-Executor",
                PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_AGENT_CHANNEL_EXECUTOR_QUEUE_SIZE);
        builder.executor(executorService);
        executorServiceMap.put(name, executorService);
    }

    private void configureChannelType(NettyChannelBuilder builder, ChannelType channelType) {
        Class<? extends Channel> channelClazz = channelType.getChannelType();
        builder.channelType(channelClazz);
    }

    private void configureEventLoopGroup(NettyChannelBuilder builder, ChannelType channelType, String name) {
        ExecutorService eventLoopExecutor = newCachedExecutorService(name + "-Channel-Worker");
        EventLoopGroup eventLoopGroup = channelType.newEventLoopGroup(1, eventLoopExecutor);
        builder.eventLoopGroup(eventLoopGroup);
        eventLoopExecutorMap.put(name, eventLoopExecutor);
        eventLoopGroupMap.put(name, eventLoopGroup);
    }

    private ChannelType getChannelType() {
        ChannelTypeFactory factory = new ChannelTypeFactory();
        PilotGrpcConstant.ChannelTypeEnum channelTypeEnum = PilotGrpcConstant.PinpointDefaultChannel.DEFAULT_CHANNEL_TYPE;
        return factory.newChannelType(channelTypeEnum);
    }

    private ExecutorService newCachedExecutorService(String name) {
        ThreadFactory threadFactory = new PilotThreadFactory(PilotThreadFactory.DEFAULT_THREAD_NAME_PREFIX + name, true);
        return Executors.newCachedThreadPool(threadFactory);
    }

    private ExecutorService newExecutorService(String name, int executorQueueSize) {
        ThreadFactory threadFactory = new PilotThreadFactory(PilotThreadFactory.DEFAULT_THREAD_NAME_PREFIX + name, true);
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(executorQueueSize);
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, workQueue, threadFactory);
    }

    @Override
    public void close() {
        if (!CollectionUtils.isEmpty(eventLoopGroupMap)) {
            eventLoopGroupMap.forEach((name, eventLoopGroup) -> {

                final Future<?> future = eventLoopGroup.shutdownGracefully();
                try {
                    log.debug("shutdown {}-eventLoopGroup", name);
                    future.await(1000 * 3);
                } catch (InterruptedException e) {
                    Thread
                            .currentThread()
                            .interrupt();
                }
            });
        }
        if (!CollectionUtils.isEmpty(eventLoopExecutorMap)) {
            eventLoopExecutorMap.forEach((name, eventLoopExecutor) -> {
                ExecutorUtils.shutdownExecutorService(name + "-eventLoopExecutor", eventLoopExecutor);
            });
        }
        if (!CollectionUtils.isEmpty(executorServiceMap)) {
            executorServiceMap.forEach((name, executorService) -> {
                ExecutorUtils.shutdownExecutorService(name + "-executorService", executorService);
            });
        }
    }

    @Override
    public Integer getCloseOrder() {
        return PilotGrpcConstant.CloseOrder.PINPOINT_GRPC_CHANNEL_CONFIGURER;
    }
}
