package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import io.grpc.Metadata;

import java.util.Objects;

/**
 * @description
 * @author tiger
 * @time 2023/5/30 9:49 AM
 */
public class PilotHeader {
    public static final Metadata.Key<String> PILOT_ID_KEY = newStringKey("pilotid");
    public static final Metadata.Key<String> PILOT_INSTANCE_ID_KEY = newStringKey("pilotinstanceid");
    public static final Metadata.Key<String> START_TIME_KEY = newStringKey("starttime");
    public static final Metadata.Key<String> SOCKET_ID = newStringKey("socketid");

    public static final String SUPPORT_COMMAND_CODE_DELIMITER = ";";

    private static Metadata.Key<String> newStringKey(String s) {
        return Metadata.Key.of(s, Metadata.ASCII_STRING_MARSHALLER);
    }

    private final String pilotId;
    private final String pilotInstanceId;
    private final Long startTime;

    public PilotHeader(String pilotId, String pilotInstanceId, long startTime) {
        this.pilotId = Objects.requireNonNull(pilotId, "pilotId");
        this.pilotInstanceId = Objects.requireNonNull(pilotInstanceId, "pilotInstanceId");
        this.startTime = startTime;
    }

    public String getPilotId() {
        return pilotId;
    }

    public String getPilotInstanceId() {
        return pilotInstanceId;
    }

    public Long getStartTime() {
        return startTime;
    }

}
