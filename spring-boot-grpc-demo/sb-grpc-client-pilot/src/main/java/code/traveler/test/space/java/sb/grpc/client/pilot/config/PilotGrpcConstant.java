

package code.traveler.test.space.java.sb.grpc.client.pilot.config;

import io.grpc.CallOptions;

import java.util.concurrent.TimeUnit;

public interface PilotGrpcConstant {

    String PILOT_GRPC_NAME = "pilot";

    interface PilotCallOptions{

        CallOptions.Key<String> PILOT_ID_KEY = CallOptions.Key.create("pilotid");

        CallOptions.Key<String> PILOT_INSTANCE_ID_KEY = CallOptions.Key.create("pilotinstanceid");

        CallOptions.Key<String> START_TIME_KEY = CallOptions.Key.create("starttime");
    }

    enum ChannelTypeEnum {
        AUTO,
        NIO,
        EPOLL
    }

    enum LBPolicyEnum {
        pick_first,
        round_robin
    }

    interface PinpointDefaultChannel {

        long DEFAULT_KEEPALIVE_TIME = TimeUnit.SECONDS.toMillis(30); // 30 seconds
        long DEFAULT_KEEPALIVE_TIMEOUT = TimeUnit.SECONDS.toMillis(60); // 60 seconds
        boolean KEEPALIVE_WITHOUT_CALLS_DISABLE = Boolean.FALSE;
        long IDLE_TIMEOUT_MILLIS_DISABLE = TimeUnit.DAYS.toMillis(30); // Disable
        int DEFAULT_MAX_HEADER_LIST_SIZE = 8 * 1024;
        int DEFAULT_MAX_MESSAGE_SIZE = 4 * 1024 * 1024;
        int DEFAULT_FLOW_CONTROL_WINDOW = 1 * 1024 * 1024; // 1MiB
        int INITIAL_FLOW_CONTROL_WINDOW = 65535;
        int DEFAULT_CONNECT_TIMEOUT = 3000;
        int DEFAULT_WRITE_BUFFER_HIGH_WATER_MARK = 32 * 1024 * 1024;
        int DEFAULT_WRITE_BUFFER_LOW_WATER_MARK = 16 * 1024 * 1024;
        ChannelTypeEnum DEFAULT_CHANNEL_TYPE = ChannelTypeEnum.AUTO;
        int DEFAULT_MAX_TRACE_EVENT = 0;
        int DEFAULT_LIMIT_COUNT = 100;
        int DEFAULT_LIMIT_TIME = 60 * 1000;
        int DEFAULT_AGENT_CHANNEL_EXECUTOR_QUEUE_SIZE = 1000;
    }

    interface CloseOrder{

        //PilotGrpcDataSender
        Integer PILOT_GRPC_DATA_SENDER = 10;

        //PinpointGrpcChannelConfigurer
        Integer PINPOINT_GRPC_CHANNEL_CONFIGURER = 20;


    }
}
