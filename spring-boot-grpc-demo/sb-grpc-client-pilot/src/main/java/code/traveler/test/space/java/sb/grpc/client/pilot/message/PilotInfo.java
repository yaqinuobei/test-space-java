

package code.traveler.test.space.java.sb.grpc.client.pilot.message;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @description
 * @author tiger
 * @time 2023/5/30 11:29 AM
 */
@Data
@Builder
public class PilotInfo implements MetaDataType {

    //pilotId，由调用链系统分配，一个k8s集群固定一个
    private String pilotId;

    //当前pilot运行的信息，对应podname或者pod uuid
    private String pilotInstanceId;

    //服务启动时间
    private Long startTime;

    //pilot包版本
    private String version;

    //pilot镜像版本
    private String pilotImageVersion;

    //默认接入的agent使用的版本
    private String agentVersion;

    //k8s的宿主机节点ip
    private List<String> nodeIps;
}
