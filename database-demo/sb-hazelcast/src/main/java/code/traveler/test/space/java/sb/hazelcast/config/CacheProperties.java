

package code.traveler.test.space.java.sb.hazelcast.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cache")
@Data
public class CacheProperties {

    //k8s namespace
    private String namespace;

    //k8s服务名
    private String serviceName;

    //节点间使用tcpIp通信
    private boolean tcpIpEnable=false;

    //节点间使用广播通信
    private boolean multicastEnable=false;

    //节点间使用k8s通信
    private boolean kubernetesEnable=true;

    //本地测试
    private boolean localTest=true;
}
