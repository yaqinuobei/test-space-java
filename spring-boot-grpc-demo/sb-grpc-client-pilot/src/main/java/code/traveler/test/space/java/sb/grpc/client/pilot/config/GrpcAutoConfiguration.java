package code.traveler.test.space.java.sb.grpc.client.pilot.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import code.traveler.test.space.java.sb.grpc.client.pilot.support.PilotThreadFactory;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.ReconnectExecutor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author panda
 * @description grpc基础配置
 * @time 2023/7/28 10:05
 */
@Configuration
public class GrpcAutoConfiguration {

    @Bean("reconnectScheduledExecutorService")
    public ScheduledExecutorService reconnectScheduledExecutorService() {
        final PilotThreadFactory threadFactory = new PilotThreadFactory("Pilot-reconnect-thread");
        final ScheduledThreadPoolExecutor scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1, threadFactory);

        ScheduledExecutorService scheduledExecutorService = Executors.unconfigurableScheduledExecutorService(scheduler);
        return scheduledExecutorService;
    }

    @Bean
    public ReconnectExecutor reconnectExecutor(@Qualifier("reconnectScheduledExecutorService") ScheduledExecutorService reconnectScheduledExecutorService) {

        // TODO custom timeout~~
        return new ReconnectExecutor(reconnectScheduledExecutorService);
    }
}
