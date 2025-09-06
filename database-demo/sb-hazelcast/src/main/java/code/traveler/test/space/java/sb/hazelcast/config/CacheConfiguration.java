

package code.traveler.test.space.java.sb.hazelcast.config;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfiguration {

    //容器化分布式hazelcast配置
    @Bean
    public Config config(CacheProperties pilotCacheProperties) {
        final Config config = new Config();
        if (pilotCacheProperties.isLocalTest()) {
            config
                    .getNetworkConfig()
                    .getJoin()
                    .getTcpIpConfig()
                    .setEnabled(true)
                    .addMember("localhost");
            config
                    .getNetworkConfig()
                    .getJoin()
                    .getMulticastConfig()
                    .setEnabled(false);
        } else {
            config
                    .getNetworkConfig()
                    .getJoin()
                    .getTcpIpConfig()
                    .setEnabled(false);
            config
                    .getNetworkConfig()
                    .getJoin()
                    .getMulticastConfig()
                    .setEnabled(false);
            config
                    .getNetworkConfig()
                    .getJoin()
                    .getKubernetesConfig()
                    .setEnabled(true)
                    .
                            setProperty(CacheConstant.Config.KUBERNETES_CONFIG_PROPERTY_NAMESPACE, pilotCacheProperties.getNamespace())
                    .setProperty(CacheConstant.Config.KUBERNETES_CONFIG_PROPERTY_SERVICE_NAME, pilotCacheProperties.getServiceName());
        }
        return config;
    }

    //hazelcast实例：操作各集合
    @Bean("hazelcastInstance")
    public HazelcastInstance hazelcastInstance(Config config) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
        return hazelcastInstance;
    }

}
