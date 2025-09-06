package code.traveler.test.space.java.sb.grpc.client.pilot.config;

import code.traveler.grpc.client.inject.GrpcClient;
import com.google.protobuf.GeneratedMessageV3;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import code.traveler.test.space.java.sb.grpc.client.pilot.message.MessageConverter;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.MetaDataType;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.PilotHeaderClientInterceptor;
import code.traveler.test.space.java.sb.grpc.client.pilot.sender.PilotGrpcDataSender;
import code.traveler.test.space.java.sb.grpc.client.pilot.service.PilotResponseHandleService;
import code.traveler.test.space.java.sb.grpc.client.pilot.support.ReconnectExecutor;
import code.traveler.test.space.java.sb.grpc.lib.pilot.PilotServiceGrpc;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@AutoConfigureAfter({GrpcAutoConfiguration.class})
public class PilotGrpcAutoConfiguration {

    //一个名称全局只有一个channel，修改channel内部参数需要慎重
    @GrpcClient(value = "pilot", interceptors = {PilotHeaderClientInterceptor.class})
    private PilotServiceGrpc.PilotServiceStub pilotServiceStub;


    @Bean
    public PilotGrpcDataSender pilotGrpcDataSender(PilotResponseHandleService pilotResponseHandleService,
                                                   @Qualifier("pilotMessageConverter") MessageConverter<MetaDataType, GeneratedMessageV3> messageConverter,
                                                   ReconnectExecutor reconnectExecutor, ScheduledExecutorService retransmissionExecutor) {

        return new PilotGrpcDataSender(pilotServiceStub, pilotResponseHandleService, messageConverter, reconnectExecutor, retransmissionExecutor);
    }

}
