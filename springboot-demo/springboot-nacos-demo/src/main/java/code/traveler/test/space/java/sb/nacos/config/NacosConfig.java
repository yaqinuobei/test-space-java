package code.traveler.test.space.java.sb.nacos.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@Slf4j
public class NacosConfig {

    @Autowired
    private NacosProperties nacosProperties;

    @Bean
    public ConfigService configService(){

        Properties properties = this.buildProperties();
        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService(properties);
        } catch (final NacosException e) {
            log.error("nacos properties:{} createConfigService excetion，异常信息：{},异常详情：{}",properties,e.getMessage(),e);
        }
        return configService;
    }

    /**
     * @description  构造properties
     * @author zhouyuxiang
     * @param
     * @return java.util.Properties
     * @time 2023/8/1 19:57
     */
    private Properties buildProperties(){
        final Properties properties = new Properties();
        properties.put("serverAddr", nacosProperties.getServerAddr());
        properties.put("username", nacosProperties.getUsername());
        properties.put("password", nacosProperties.getPassword());
        properties.put("namespace", nacosProperties.getNamespace());
        return properties;
    }
}
