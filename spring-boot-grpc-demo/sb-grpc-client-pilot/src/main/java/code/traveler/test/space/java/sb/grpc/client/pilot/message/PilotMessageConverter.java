package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import com.google.protobuf.GeneratedMessageV3;
import org.springframework.stereotype.Component;

import code.traveler.test.space.java.sb.grpc.lib.pilot.PPilotInfo;

/**
 * @author original
 */
@Component("pilotMessageConverter")
public class PilotMessageConverter implements MessageConverter<MetaDataType, GeneratedMessageV3> {

    @Override
    public GeneratedMessageV3 toMessage(MetaDataType message) {
        if (message instanceof PilotInfo) {
            final PilotInfo agentInfo = (PilotInfo) message;
            return convertAgentInfo(agentInfo);
        }
        return null;
    }


    public PPilotInfo convertAgentInfo(final PilotInfo agentInfo) {

        final PPilotInfo.Builder builder = PPilotInfo.newBuilder();
        builder.setAgentVersion(agentInfo.getAgentVersion());
        builder.setPilotId(agentInfo.getPilotId());
        builder.setPilotImageVersion("v2.22");
        builder.setPilotInstanceId("156055");
        builder.setVersion("v5555");
        builder.setStartTime(15654656656L);
        for (int i = 0; i < agentInfo
                .getNodeIps()
                .size(); i++) {

            builder.setNodeIps(i, agentInfo
                    .getNodeIps()
                    .get(i));
        }

        return builder.build();
    }


}
