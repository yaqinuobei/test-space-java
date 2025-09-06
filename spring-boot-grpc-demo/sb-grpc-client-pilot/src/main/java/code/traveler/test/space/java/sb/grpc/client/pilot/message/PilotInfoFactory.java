package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class PilotInfoFactory {

    private String pilotId="1";
    private String pilotInstanceId="2";
    private Long startTime = new Date().getTime();

    public PilotInfo createPilotInfo(){
        List<String> nodeIps = new ArrayList<String>(){{
            add("127.0.0.1");
        }};
        return PilotInfo.builder().agentVersion("V1.0.0").pilotId("1122").nodeIps(nodeIps).pilotImageVersion("v2.22")
                .pilotInstanceId("156055").version("v5555").startTime(15654656656L).build();
    }

    public PilotHeader createPilotHeader(){

        return new PilotHeader(pilotId,pilotInstanceId,startTime);
    }
}
