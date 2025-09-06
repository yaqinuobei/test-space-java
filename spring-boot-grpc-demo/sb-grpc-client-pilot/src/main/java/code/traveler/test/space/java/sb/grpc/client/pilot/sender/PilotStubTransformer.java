package code.traveler.test.space.java.sb.grpc.client.pilot.sender;

import code.traveler.grpc.client.inject.StubTransformer;
import io.grpc.stub.AbstractStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import code.traveler.test.space.java.sb.grpc.client.pilot.config.PilotGrpcConstant;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.PilotHeader;
import code.traveler.test.space.java.sb.grpc.client.pilot.message.PilotInfoFactory;

/**
 *@description pilot stub转化处理
 *@author panda
 *@time 2023/7/27 23:43
 */
@Component
public class PilotStubTransformer implements StubTransformer {

    @Autowired
    private PilotInfoFactory pilotInfoFactory;

    @Override
    public AbstractStub<?> transform(String name, AbstractStub<?> abstractStub) {
        if(name.equals(PilotGrpcConstant.PILOT_GRPC_NAME)){
            PilotHeader pilotHeader = pilotInfoFactory.createPilotHeader();
            //添加信息便于头部提取传递
            return abstractStub.withOption(PilotGrpcConstant.PilotCallOptions.PILOT_ID_KEY,pilotHeader.getPilotId())
                    .withOption(PilotGrpcConstant.PilotCallOptions.PILOT_INSTANCE_ID_KEY,pilotHeader.getPilotInstanceId())
                    .withOption(PilotGrpcConstant.PilotCallOptions.START_TIME_KEY,pilotHeader.getStartTime().toString());
        }
        return abstractStub;
    }
}
